package com.bitbucket.actor

import akka.actor.{Address, Actor, ActorLogging}
import akka.cluster.ClusterEvent.{MemberEvent, MemberRemoved, MemberUp, CurrentClusterState}
import akka.cluster.{Cluster, MemberStatus}
import akka.contrib.pattern.DistributedPubSubExtension
import akka.contrib.pattern.DistributedPubSubMediator.Publish
import com.bitbucket.ClusterSystem
import com.bitbucket.model.{NewNode, UpdateDelay}


class NodeListener extends Actor with ActorLogging {
  var nodes = Set.empty[Address]

  var jmx: Option[NodeManagementJmx] = None

  override def preStart(): Unit = {
    jmx = {
      val newJmx = new NodeManagementJmx(self)
      newJmx.createMBean()
      Some(newJmx)
    }
  }

  def receive = {
    case state: CurrentClusterState =>
      nodes = state.members.collect {
        case m if m.status == MemberStatus.Up => m.address
      }
    case MemberUp(member) =>
      nodes += member.address
      log.info("Member is Up: {}. {} nodes in cluster",
        member.address, nodes.size)
    case MemberRemoved(member, _) =>
      nodes -= member.address
      log.info("Member is Removed: {}. {} nodes cluster",
        member.address, nodes.size)

    case _: MemberEvent => // ignore

    case msg @ UpdateDelay(d) =>
      log.info(s"Updating delay from JMX to $d")
      DistributedPubSubExtension(context.system).mediator ! Publish(NodeActor.topic, msg)

    case NewNode =>
      log.info("A new node request via JMX")
      ClusterSystem.newNode()
  }
}
