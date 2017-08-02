name := "Streams Playground"

version := "1.0"

scalaVersion := "2.12.3"

val akkaVersion = "2.5.3"

val akkaCoreDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

libraryDependencies := akkaCoreDeps

