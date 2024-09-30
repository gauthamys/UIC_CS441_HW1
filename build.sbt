val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "hw1",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "com.knuddels" % "jtokkit" % "1.1.0",
      "org.deeplearning4j" % "deeplearning4j-nlp" % "1.0.0-M2.1",
      "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-M2.1",
      "org.nd4j" % "nd4j-native-platform" % "1.0.0-M2.1",
      "org.bytedeco" % "openblas-platform" % "0.3.26-1.5.10",
      "org.bytedeco" % "openblas" % "0.3.26-1.5.10",
      "org.apache.hadoop" % "hadoop-common" % "3.4.0",
      "org.apache.hadoop" % "hadoop-hdfs" % "3.4.0",
      "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "3.4.0",
      "org.apache.hadoop" % "hadoop-mapreduce-client-common" % "3.4.0",
      "org.apache.hadoop" % "hadoop-mapreduce-client-jobclient" % "3.4.0",
      "org.apache.hadoop" % "hadoop-yarn-common" % "3.4.0",
    ),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", _@xs_*) => MergeStrategy.discard
      case PathList("META-INF", "services", _@xs_*) => MergeStrategy.concat
      case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
      case "reference.conf" => MergeStrategy.concat
      case _ => MergeStrategy.first
    }
  )


