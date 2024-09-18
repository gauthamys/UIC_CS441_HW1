val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "hw1",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
      libraryDependencies += "com.knuddels" % "jtokkit" % "1.1.0"

  )

