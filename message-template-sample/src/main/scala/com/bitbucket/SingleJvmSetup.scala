package com.bitbucket

import akka.actor.{Props, ActorSystem}
import akka.cluster.Cluster
import com.typesafe.scalalogging.StrictLogging
import com.bitbucket.actor.{NodeListener, NodeActor}


object SingleJvmSetup extends App with StrictLogging {
  {
    logger.warn("Running a setup for a single JVM, 1 node with 4 actors")

    val system = ActorSystem(ClusterSystem.Name)

    // Node listener serves as a node management utility, so be it
    system.actorOf(Props[NodeListener], "nodeListener")

    val joinAddress = Cluster(system).selfAddress
    Cluster(system).join(joinAddress)

    system.actorOf(Props[NodeActor], "actor1")
    system.actorOf(Props[NodeActor], "actor2")
    system.actorOf(Props[NodeActor], "actor3")
  }
}
