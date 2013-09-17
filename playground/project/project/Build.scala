import sbt._
object PluginDef extends Build {
   override lazy val projects = Seq(root)
   lazy val root = Project("plugins", file(".")) dependsOn( myPluging )
   lazy val myPluging = uri("file:////home/kalmanb/work/sbt-dependency-builder/")
}
