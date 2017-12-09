name := "scalax15-slick"

version := "1.0"

scalaVersion := "2.12.4"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xlint",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"           % "3.2.1",
  "com.h2database"      % "h2"              % "1.4.196",
  "ch.qos.logback"      % "logback-classic" % "1.2.3",
  "com.lihaoyi"         % "ammonite-repl"   % "1.0.3-1-cd346fb" cross CrossVersion.full
)

val ammoniteInitialCommands = """
  |import slick.jdbc.H2Profile.api._
  |import scala.concurrent._
  |import scala.concurrent.duration._
  |import scala.concurrent.ExecutionContext.Implicits.global
  |repl.prompt() = "scala> "
  |repl.colors() = ammonite.repl.Colors.Default.copy(
  |  prompt  = ammonite.repl.Ref(Console.BLUE),
  |  `type`  = ammonite.repl.Ref(Console.CYAN),
  |  literal = ammonite.repl.Ref(Console.YELLOW),
  |  comment = ammonite.repl.Ref(Console.WHITE),
  |  keyword = ammonite.repl.Ref(Console.RED)
  |)
""".trim.stripMargin

initialCommands in console := s"""
  |ammonite.repl.Repl.run(\"\"\"$ammoniteInitialCommands\"\"\")
""".trim.stripMargin
