package com.bitbucket.actor

import akka.actor.{ActorLogging, Actor}
import akka.contrib.pattern.DistributedPubSubExtension
import akka.contrib.pattern.DistributedPubSubMediator.{Unsubscribe, Publish, Subscribe}
import scala.concurrent.duration._
import com.bitbucket.model.{UpdateDelay, Message}


object NodeActor {
  val topic = "distributed-messaging"

  case object Tick
}

class NodeActor extends Actor with ActorLogging {
  import NodeActor._
  import context.dispatcher

  var total = 0L

  val startTime = System.currentTimeMillis()

  def scheduler = context.system.scheduler

  val mediator = DistributedPubSubExtension(context.system).mediator

  var tick: FiniteDuration = 60.seconds

  override def preStart(): Unit = {
    mediator ! Subscribe(topic, self)
    reschedule()
  }

  override def postStop(): Unit = {
    mediator ! Unsubscribe(topic, self)
  }

  def reschedule(): Unit = {
    context.system.scheduler.scheduleOnce(tick, self, Tick)
  }

  def receive = {
    case Tick =>
      reschedule()
      mediator ! Publish(topic, Message("<The message!>"))

    case Message(text) =>
      total += 1
      val timeDiff = System.currentTimeMillis() - startTime
      val avgSpeed = total * 1000f / timeDiff
      log.info(s"Got message: $text of total $total at avg speed (messages/sec): $avgSpeed")

    case UpdateDelay(d) =>
      log.info(s"Updating delay $d")
      tick = d
      reschedule()

  }
}
