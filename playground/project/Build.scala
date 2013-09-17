import sbt._
import sbt.Project._
import Keys._
import com.kalmanb.sbt.DependencyBuilderPlugin._

object SbtJenkins extends Build {
  val projectName = "sbt-test"
  val buildVersion = "0.1.0-SNAPSHOT"
  val Org = "com.kalmanb"

  //lazy val root = Project(
  //id = projectName,
  //base = file("."),
  //settings = Seq(
  //version := buildVersion,
  //libraryDependencies ++= Seq()
  //) ++ Project.defaultSettings ++ dependencyBuilderSettings)

  lazy val moduleone = Project(
    id = "moduleone",
    base = file("moduleone"),
    settings = Seq(
      organization := Org,
      version := buildVersion,
      libraryDependencies ++= Seq(Org %% "moduletwo" % buildVersion)
    ) ++ defaultSettings ++ dependencyBuilderSettings
  )

  lazy val moduletwo = Project(
    id = "moduletwo",
    base = file("moduletwo"),
    settings = Seq(
      organization := Org,
      version := buildVersion,
      libraryDependencies ++= Seq(Org %% "modulethree" % buildVersion)
    ) ++ defaultSettings ++ dependencyBuilderSettings
  )

  lazy val modulethree = Project(
    id = "modulethree",
    base = file("modulethree"),
    settings = Seq(
      organization := Org,
      version := buildVersion
    ) ++ defaultSettings
  )
}

