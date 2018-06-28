package com.bitbucket

import akka.actor.{Props, ActorSystem, Address}
import akka.cluster.Cluster
import com.bitbucket.actor.NodeActor
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging


object ClusterSystem extends LazyLogging {
  val Name = "ClusterSystem"

  val seedAddresses = (Vector(2551, 2552) map { port: Int =>
    Address("akka.tcp", ClusterSystem.Name, "127.0.0.1", port)
  }).toSeq

  /**
    * An utility method for instantiating a new node
    * with 4 'worker' actors included.
    */
  def newNode(): Unit = {
    logger.info("Appending a new node to the cluster...")

    val conf = ConfigFactory.parseString("akka.remote.netty.tcp.port=0")
      .withFallback(ConfigFactory.load())
    val system = ActorSystem(ClusterSystem.Name, conf)

    system.actorOf(Props[NodeActor], "actor1")
    system.actorOf(Props[NodeActor], "actor2")
    system.actorOf(Props[NodeActor], "actor3")
    system.actorOf(Props[NodeActor], "actor4")

    ClusterSystem.seedAddresses foreach { addr =>
      Cluster(system).join(addr)
    }
  }
}
