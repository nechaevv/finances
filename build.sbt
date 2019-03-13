import org.scalajs.core.tools.linker.backend.OutputMode
import sbtcrossproject.CrossPlugin.autoImport.crossProject

val sharedSettings = Seq(
  organization := "com.github.nechaevv.finances",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.12.8"
)

val monocleVersion = "1.5.1-cats"
val circeVersion = "0.11.1"
val doobieVersion = "0.6.0"
val Http4sVersion = "0.20.0-M6"

lazy val root = crossProject(JSPlatform, JVMPlatform).in(file("."))
  .settings(name := "finances", version := "0.1-SNAPSHOT")
  .settings(sharedSettings)
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.github.nechaevv.isomorphic" %% "isomorphic-core" % "0.1-SNAPSHOT",
      "org.http4s"    %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"    %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"    %% "http4s-circe"        % Http4sVersion,
      "org.http4s"    %% "http4s-dsl"          % Http4sVersion,
      "io.circe"      %% "circe-core"       % circeVersion,
      "io.circe"      %% "circe-generic"    % circeVersion,
      "io.circe"      %% "circe-parser"     % circeVersion,
      "org.tpolecat"  %% "doobie-core"      % doobieVersion,
      "org.tpolecat"  %% "doobie-hikari"    % doobieVersion,
      "org.tpolecat"  %% "doobie-postgres"  % doobieVersion,
      "org.tpolecat"  %% "doobie-scalatest" % doobieVersion % "test",
      "org.postgresql" % "postgresql"       % "42.2.5",
      "ch.qos.logback" % "logback-classic"  % "1.2.3",
      "com.typesafe"   % "config"           % "1.3.2",
      "org.scalatest" %% "scalatest"        % "3.0.5" % "test",
      "org.scala-lang.modules" %% "scala-xml" % "1.1.1"
    ),
    scalacOptions ++= Seq("-Ypartial-unification", "-feature", "-deprecation"),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9")
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "com.github.nechaevv.isomorphic" %%% "isomorphic-core" % "0.1-SNAPSHOT",
      "io.circe"      %%% "circe-core"    % circeVersion,
      "io.circe"      %%% "circe-generic" % circeVersion,
      "io.circe"      %%% "circe-parser"  % circeVersion,
      "com.github.julien-truffaut" %%%  "monocle-core"  % monocleVersion,
      "com.github.julien-truffaut" %%%  "monocle-macro" % monocleVersion,
      "com.github.julien-truffaut" %%%  "monocle-law"   % monocleVersion % "test",
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0-M13",
      "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.0.0-M13_2018c"
    ),
    scalacOptions ++= Seq("-P:scalajs:sjsDefinedByDefault", "-feature", "-deprecation"),
    scalaJSOutputMode := OutputMode.ECMAScript6,
    scalaJSLinkerConfig := scalaJSLinkerConfig.value
      .withRelativizeSourceMapBase(Some((artifactPath in (Compile, fastOptJS)).value.toURI))
      .withModuleKind(ModuleKind.ESModule),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )

