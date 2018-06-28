organization := "ma.thele"

name := "aggro"

version := "0.1"

scalaVersion := "2.11.5"

mainClass := Some("Main")

val akkaV = "2.3.12"

val akkaStreamV = "1.0"

val akkaHttpV = "1.0"

val sprayV = "1.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV,
  "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-http-core-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaStreamV % "test",
  "com.typesafe.akka" %% "akka-stream-experimental" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-core-experimental" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaHttpV,
  "io.spray"            %%  "spray-json" % "1.3.1",
  "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
  "com.github.nscala-time" %% "nscala-time" % "1.8.0",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test"
)


