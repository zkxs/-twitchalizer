import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.12.4"
    )),
    organization := "net.michaelripley",
    name := "twitchalizer",
    version := "0.0.0-SNAPSHOT",
    resolvers ++= extraResolvers,
    libraryDependencies ++= testLibraries,
    libraryDependencies ++= dependencies,
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    mainClass := Some("net.michaelripley.twitchalizer.Driver")
  )
