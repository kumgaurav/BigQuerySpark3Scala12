name := "BigQuerySpark3Scala12"
version := "0.1"
scalaVersion := "2.12.13"
val sparkVersion = "3.1.1"

lazy val commonSettings = Seq(
  organization := "id_10",
  name := "BigQuerySpark3Scala12",
  version := "0.1",
  scalaVersion := "2.12.13"
)

lazy val shaded = (project in file("."))
  .settings(commonSettings)

resolvers ++= Seq(
  Classpaths.typesafeReleases,
  "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"
)
resolvers += Resolver.mavenLocal

lazy val commonTestDependencies = Seq(
	"org.scalatest" %% "scalatest" % "3.0.5" % "test",
	"junit" % "junit" % "4.12" % "test",
	"org.specs2" %% "specs2-core" % "4.2.0" % "test",
	"org.specs2" %% "specs2-junit" % "4.2.0" % "test"
)

libraryDependencies ++= (commonTestDependencies ++ Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  // type-safe
  "com.typesafe" % "config" % "1.3.1",
  "com.google.cloud.spark" %% "spark-bigquery-with-dependencies" % "0.20.0" % "provided",
  "com.google.cloud" % "google-cloud-bigquery" % "1.123.2",
  "com.google.cloud" % "google-cloud-bigquerystorage" % "1.6.0" exclude("io.grpc", "grpc-netty-shaded"),
  
))

assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", xs @ _*) => MergeStrategy.last
  case PathList("com", "sun", xs @ _*) => MergeStrategy.last
  case PathList("google", "protobuf", xs @ _*) => MergeStrategy.first
  case PathList("org", "apache", "httpcomponents", xs @ _*) => MergeStrategy.first
  case PathList("org", "apache", "spark", xs @ _*) => MergeStrategy.discard
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("org", "w3c", xs @ _*) => MergeStrategy.last
  case PathList("org", "xml", xs @ _*) => MergeStrategy.last
  case PathList("org", "objenesis", xs @ _*) => MergeStrategy.last
  case PathList("org", "objectweb", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case PathList("org", "json4s", xs @ _*) => MergeStrategy.first
  case PathList("org", "slf4j", xs @ _*) => MergeStrategy.first
  case PathList("com", "crealytics", xs @ _*) => MergeStrategy.last
  case PathList("scala", xs @ _*) => MergeStrategy.last
  case PathList("org", "joda", xs @ _*) => MergeStrategy.last
  case PathList("net", "jpountz", xs @ _*) => MergeStrategy.last
  case PathList("org", "joda", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "overview.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "parquet.thrift" => MergeStrategy.last
  case "plugin.xml" => MergeStrategy.last
  case "META-INF/git.properties" => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.last
  case "library.properties" => MergeStrategy.first
  case "scala-xml.properties" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}