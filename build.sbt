organization := "com.kalmanb"
            
name := "sbt-dependency-builder"

version := "0.1.0-SNAPSHOT"

sbtPlugin := true

publishMavenStyle := false

publishArtifact in Test := false

//sbtVersion in Global := "0.13.0"
sbtVersion in Global := "0.12.4"

//publishTo := Some(Resolver.url("repo", url("http://"))(Resolver.ivyStylePatterns))

libraryDependencies ++= Seq(
    //"org.scalatest" %% "scalatest" % "2.0.RC1-SNAP4" % "test" // scala 2.10.2
    //"junit" % "junit" % "4.11" % "test"
)


