import ReleaseTransformations._

val SCALA_2_12 = "2.12.2"
scalaVersion in ThisBuild := SCALA_2_12

val buildSettings = Seq[Setting[_]](
  name := "Gracase",
  scalaVersion := SCALA_2_12,
//  crossScalaVersions := Seq(SCALA_2_12),
  organization := "com.lewuathe",
//  crossPaths := true,
  publishMavenStyle := true,
//  incOptions := incOptions.value
//    .withNameHashing(true)
//    // Suppress macro recompile warning: https://github.com/sbt/sbt/issues/2654
//    .withLogRecompileOnMacro(false),
  logBuffered in Test := false,
  updateOptions := updateOptions.value.withCachedResolution(true),
  scalacOptions ++= Seq("-feature", "-deprecation"),
  sonatypeProfileName := "com.lewuathe",
  licenses += ("Apache-2.0", url(
    "https://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/lewuathe/Gracase")),
  // Use sonatype resolvers
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("scalameta", "maven")
  ),
  // Release settings
  releaseTagName := { (version in ThisBuild).value },
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _),
                enableCrossBuild = true),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _),
                enableCrossBuild = true),
    pushChanges
  ),
  releaseCrossBuild := true,
  publishTo := Some(
    if (isSnapshot.value) {
      Opts.resolver.sonatypeSnapshots
    } else {
      Opts.resolver.sonatypeStaging
    }
  )
)

val macroAnnotationSettings = Seq[Setting[_]](
  addCompilerPlugin(
    "org.scalameta" % "paradise" % "3.0.0-M9" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise"
//  scalacOptions in (Compile, console) := Seq()
)

lazy val gracaseRoot =
  Project(id = "gracase", base = file("."))
    .settings(
      macroAnnotationSettings,
      buildSettings,
      libraryDependencies ++= Seq(
        "org.scalameta" %% "scalameta" % "1.8.0",
        "org.scalameta" %% "contrib"   % "1.8.0",
        "org.scalatest" %% "scalatest" % "3.0.1" % "test"
      )
    )
