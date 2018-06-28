import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

val akkaVer = "2.3.9"

val project = Project(
  id = "akka-messaging-template",
  base = file("."),
  settings = Project.defaultSettings ++ SbtMultiJvm.multiJvmSettings ++ Seq(
    organization := "com.bitbucket",
    name := "akka-messaging-template",
    version := "0.1",
    scalaVersion := "2.11.5",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-remote" % akkaVer,
      "com.typesafe.akka" %% "akka-contrib" % akkaVer,
      "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVer,
      "com.typesafe.akka" % "akka-cluster_2.11" % akkaVer,
      "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
      "org.scalatest" %% "scalatest" % "2.2.1" % "test"),
      // this one is used with sbt:run
    mainClass := Some("SingleJvmSetup"),
      // make sure that MultiJvm test are compiled by the default test compilation
    compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
    // disable parallel tests
    parallelExecution in Test := false,
    // make sure that MultiJvm tests are executed by the default test target,
    // and combine the results from ordinary test and multi-jvm tests
    executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
      case (testResults, multiNodeResults)  =>
        val overall =
          if (testResults.overall.id < multiNodeResults.overall.id)
            multiNodeResults.overall
          else
            testResults.overall
        Tests.Output(overall,
          testResults.events ++ multiNodeResults.events,
          testResults.summaries ++ multiNodeResults.summaries)
    }
  )
) configs (MultiJvm)

