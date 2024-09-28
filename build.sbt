Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / usePipelining := true

ThisBuild / scalaVersion := "3.5.1"

name := "k8s-gantry"

fork := true

Compile / mainClass := Some("gantry.Main")

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Wunused:all",
  "-Wnonunit-statement",
  "-Wvalue-discard",
  "-experimental"
)

val Versions = new {
    val osLib     = "0.10.7"
    val caseApp   = "2.1.0-M29"
    val scalaYaml = "0.3.0"
}

libraryDependencies ++= List(
  "com.lihaoyi"                %% "os-lib"     % Versions.osLib,
  "com.github.alexarchambault" %% "case-app"   % Versions.caseApp,
  "org.virtuslab"              %% "scala-yaml" % Versions.scalaYaml
)
