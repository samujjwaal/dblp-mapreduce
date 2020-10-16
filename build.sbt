name := "hw2"

version := "0.1"

scalaVersion := "2.13.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

mainClass in(Compile, run) := Some("com.samujjwaal.hw2.RunJobs")
mainClass in(Compile, packageBin) := Some("com.samujjwaal.hw2.RunJobs")

enablePlugins(AssemblyPlugin)

// META-INF discarding
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
assemblyJarName in assembly := "hw2_dblp_mapred.jar"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(cacheOutput = false)

// https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

// https://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "com.typesafe" % "config" % "1.4.0"

// https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-common
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "3.3.0"

// https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-mapreduce-client-core
libraryDependencies += "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "3.3.0"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.3.0"

// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"

// https://mvnrepository.com/artifact/org.scalactic/scalactic
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"



