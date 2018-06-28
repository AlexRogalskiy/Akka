package com.bitbucket

import akka.actor.{Address, Props, ActorSystem}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.ClusterDomainEvent
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import com.bitbucket.actor.{NodeActor, NodeListener}


object DefaultSetupMultiJvmSeedNode1 extends StrictLogging with App {
  {
    logger.info("Starting seeding node 1")

    val conf = ConfigFactory.parseString("akka.remote.netty.tcp.port=2551")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles = [leader]"))
      .withFallback(ConfigFactory.load())

    val system = ActorSystem(ClusterSystem.Name, conf)

    val nodeListener = system.actorOf(Props[NodeListener], "leader")

    val selfAddress = Cluster(system).selfAddress
    Cluster(system).join(selfAddress)

    // this should be done only once
    Cluster(system).subscribe(nodeListener, classOf[ClusterDomainEvent])
  }
}

object DefaultSetupMultiJvmSeedNode2 extends StrictLogging with App {
  {
    // the delay is intended to init the first seed node the first
    Thread.sleep(1000)
    logger.info("Starting seeding node 2")

    val conf = ConfigFactory.parseString("akka.remote.netty.tcp.port=2552")
      .withFallback(ConfigFactory.load())
    val system = ActorSystem(ClusterSystem.Name, conf)

    system.actorOf(Props[NodeListener], "leader")
    system.actorOf(Props[NodeActor], "actor1")

    val addresses = Vector(2551) map { port =>
      Address("akka.tcp", ClusterSystem.Name, "127.0.0.1", port)
    }

    Cluster(system).joinSeedNodes(addresses)
  }
}

object DefaultSetupMultiJvmTestNode3 extends StrictLogging with App {
  {
    // the delay is intended to init the seed nodes first
    Thread.sleep(2000)
    logger.info("Starting node 3")

    val system = ActorSystem(ClusterSystem.Name)

    system.actorOf(Props[NodeListener], "leader")
    system.actorOf(Props[NodeActor], "actor1")
    system.actorOf(Props[NodeActor], "actor2")

    ClusterSystem.seedAddresses foreach { addr =>
      Cluster(system).join(addr)
    }
  }
}