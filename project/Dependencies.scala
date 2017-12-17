import sbt._

object Dependencies {

  lazy val dependencies = Seq(
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "org.slf4j" % "slf4j-log4j12" % "1.7.25",
    "log4j" % "log4j" % "1.2.17",
    "org.json4s" %% "json4s-jackson" % "3.5.3",
    "org.asynchttpclient" % "async-http-client" % "2.1.0-RC2",
    "org.asynchttpclient" % "async-http-client-extras-guava" % "2.1.0-RC2",
    "com.google.guava" % "guava" % "23.5-jre"
  )

  lazy val testLibraries: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.0.4"
  ).map(_ % Test)

  lazy val extraResolvers = Seq()

}
