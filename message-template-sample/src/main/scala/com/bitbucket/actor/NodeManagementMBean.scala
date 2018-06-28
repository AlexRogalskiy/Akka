package com.bitbucket.actor

import java.lang.management.ManagementFactory
import javax.management.{InstanceAlreadyExistsException, StandardMBean, ObjectName}
import scala.concurrent.duration._
import akka.actor.ActorRef
import com.bitbucket.model.{NewNode, UpdateDelay}


trait NodeManagementMBean {
  def updateDelay(delay: Int): Unit

  def newNode(): Unit
}

private[actor] class NodeManagementJmx(ref: ActorRef) {
  private val mBeanServer = ManagementFactory.getPlatformMBeanServer
  private val nodeMngMBeanName = new ObjectName("akka:type=NodeManagement")

  def createMBean() = {
    val mbean = new StandardMBean(classOf[NodeManagementMBean]) with NodeManagementMBean {
      override def updateDelay(delay: Int): Unit = {
        if (delay < 1 || delay > 100)
          throw new IllegalArgumentException("Delay must conform the rules: d > 0 and d <= 100")
        ref ! UpdateDelay(delay.millis)
      }

      override def newNode(): Unit = {
        ref ! NewNode
      }
    }
    try {
      mBeanServer.registerMBean(mbean, nodeMngMBeanName)
    } catch {
      case e: InstanceAlreadyExistsException => // ignoring
    }
  }

}
