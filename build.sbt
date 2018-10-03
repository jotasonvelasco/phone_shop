import sbt.Keys._
import com.typesafe.sbt.packager.docker._

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .aggregate(orderProcessor, inventoryManagement)

lazy val inventoryManagement = project
  .in(file("inventory-management"))
  .enablePlugins(PlayScala)
  .settings(
    name := """inventory-management""",
    settings,
    libraryDependencies ++= commonDependencies,
    dockerExposedPorts := Seq(9000)
  )

lazy val orderProcessor = project
  .in(file("order-processor"))
  .enablePlugins(PlayScala)
  .settings(
    name := """order-processor""",
    settings,
    libraryDependencies ++= commonDependencies,
    dockerExposedPorts := Seq(9003)
  )


lazy val settings = commonSettings ++ dockerSettings

lazy val commonSettings = Seq(
  organization := "com.jotason.phone",
  version := "0.1.0-SNAPSHOT",
  resolvers += Resolver.typesafeRepo("releases"),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  scalaVersion := "2.12.6",
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8", // yes, this is 2 args
    "-target:jvm-1.8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen"
  ),
  scalacOptions in Test ++= Seq("-Yrangepos"),
  autoAPIMappings := true
)

lazy val dockerSettings = Seq(
  dockerCommands := dockerCommands.value.filterNot {
    // ExecCmd is a case class, and args is a varargs variable, so you need to bind it with @
    case Cmd("USER", args@_*) => true
    // dont filter the rest
    case cmd => false
  },
  version in Docker := "latest",
  maintainer in Docker := "jose.velasco@gmail.com",
  dockerBaseImage := "java:8"
)

val commonDependencies = Seq(
  guice,
  ws,
  "org.joda" % "joda-convert" % "1.9.2",
  "net.logstash.logback" % "logstash-logback-encoder" % "4.11",
  "com.netaporter" %% "scala-uri" % "0.4.16",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.mockito" % "mockito-core" % "2.7.22",
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
  "org.postgresql" % "postgresql" % "9.4.1209",
  "com.h2database" % "h2" % "1.4.197" % Test,
  "org.scalacheck" %% "scalacheck" % "1.13.+"
)