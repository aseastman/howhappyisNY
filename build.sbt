

resolvers ++= Seq("mvnrepository" at "http://mvnrepository.com/artifact/",
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
  "Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/")

val spark_version = "1.5.0"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ws" % "2.4.3",
  "io.netty" % "netty" % "3.10.1.Final" force(),
  "org.apache.spark" %% "spark-core" % spark_version,
  "org.apache.spark" %% "spark-sql" % spark_version,
  "org.apache.spark" %% "spark-hive" % spark_version,
  "org.apache.spark" % "spark-streaming-twitter_2.10" % spark_version,
  "org.apache.spark" % "spark-streaming_2.10" % spark_version,
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"
)

dependencyOverrides += "org.apache.httpcomponents" % "httpclient" % "4.5.2"

dependencyOverrides += "org.apache.httpcomponents" % "httpcore" % "4.4.4"

dependencyOverrides += "io.netty" % "netty" % "3.10.1.Final"

lazy val root = (project in file("."))
  .settings(
    name := "Spark-H",
    version := "0.1"
  )