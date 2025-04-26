Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "3.6.4"

name := "k8s-gantry"

fork := true

Compile / mainClass := Some("gantry.cli.Main")

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-experimental",
  "-Wall",
)

val Versions = new {
    val osLib           = "0.10.7"
    val caseApp         = "2.1.0-M29"
    val scalaYaml       = "0.3.0"
    val lightbendConfig = "1.4.3"
    val pureConfig      = "0.17.9"
}

libraryDependencies ++= List(
  "com.lihaoyi"                %% "os-lib"                    % Versions.osLib,
  "com.github.alexarchambault" %% "case-app"                  % Versions.caseApp,
  "org.virtuslab"              %% "scala-yaml"                % Versions.scalaYaml,
  "com.typesafe"                % "config"                    % Versions.lightbendConfig,
  "com.github.pureconfig"      %% "pureconfig-core"           % Versions.pureConfig,
  "com.github.pureconfig"      %% "pureconfig-generic-scala3" % Versions.pureConfig
)
