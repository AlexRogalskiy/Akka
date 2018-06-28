# Akka messaging template #

This application may serve as a template for distributed applications built upon [akka-cluster](http://doc.akka.io/docs/akka/snapshot/common/cluster.html).

### How to run ###

    git clone https://bitbucket.org/euk/akka-messaging-template.git

    cd akka-messaging-template

    sbt

Then, in sbt shell:

    multi-jvm:run com.bitbucket.DefaultSetup

A default cluster setup contains 3 cluster nodes, each of them in a separate JVM.
Each cluster node contains from 1 to 5 worker actors, each of them using [DistributedPubSubExtension](http://doc.akka.io/api/akka/2.3.1/index.html#akka.contrib.pattern.DistributedPubSubExtension$) for message broadcasting. Please note that the initial delay is set to 60 seconds for the convenience and should be updated via JMX's __updateDelay(...)__.

### JMX-based node management ###

## NodeManagement ##
Some basic utility methods are exposed via JMX (any JMX-console would suffice, e. g. [JMC](http://www.oracle.com/technetwork/java/javaseproducts/mission-control/java-mission-control-1998576.html)). You may find two utility routines in MBean tree under __akka__ folder. Here's a short description of __NodeManagement__ capabilities:

| Function name | Description             |
| ------------- | ------------------------------ |
| `updateDelay(millis: int)`      | Updates the messaging delay for all cluster nodes.   |
| `newNode()`   | Starts a new cluster node with 4 worker actors.    |

## Cluster ##

You may also be interested in the fine tuning of cluster nodes. This might be done using standard __akka-cluster__ JMX operations. The bean __Cluster__ resides in the __akka__ section of the MBean tree as well. Available operations are:

| Function name | Description             |
| ------------- | ------------------------------ |
| `down(addr: String)`      | Shutdowns the node at address __addr__.   |
| `join(addr: String)`   | Joins a detached node to the cluster.    |
| `leave(addr: String)`   | Urges the node at address __addr__ to leave the cluster.   |