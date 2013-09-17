import sbt.Keys._
import sbt.Project._
import sbt._

import com.kalmanb.sbt.DependencyBuilderPlugin._

object SbtJenkins extends Build {
  val projectName = "sbt-test"
  val buildVersion = "0.1.0-SNAPSHOT"
  val Org = "com.kalmanb"

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
      version := buildVersion
      //libraryDependencies ++= Seq(Org %% "modulethree" % buildVersion)
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

