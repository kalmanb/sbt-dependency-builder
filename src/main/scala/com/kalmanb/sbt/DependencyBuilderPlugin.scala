package com.kalmanb.sbt

import sbt.Keys._
import sbt.Load.BuildStructure
import sbt._

object DependencyBuilderPlugin extends Plugin {

  val kalKey = TaskKey[Unit]("kal")
  val dependencyBuilderSettings = Seq[Setting[_]](
    kalKey <<= (thisProjectRef, buildStructure, state) map {
      (thisProjectRef, structure, state) ⇒
        update(thisProjectRef, state)
    }
  )

  def update(project: ProjectRef, state: State): Unit = {

    val missingDependencies: Seq[ModuleID] = getMissingDependencies(project, state)
    val allProjectRefs = Project.extract(state).structure.allProjectRefs

    val modulesToBuild = allProjectRefs.filter(ref ⇒ missingDependencies.exists(d ⇒ d.name startsWith ref.project))

    //modulesToBuild foreach (publishLocalModule(_, state))
    modulesToBuild foreach (update(_, state))

    evaluateTask(Keys.publishLocal in configuration, project, state)
  }

  def getMissingDependencies(ref: ProjectRef, state: State): Seq[ModuleID] = {
    evaluateTask(Keys.update in configuration, ref, state) match {
      case Some((_, Inc(inc))) ⇒ {
        inc.causes.flatMap(_.directCause.map(_ match {
          case e: sbt.ResolveException ⇒ e.failed
          case _                       ⇒ Seq.empty
        }))
      }.flatten
      case _ ⇒ Seq.empty
    }
  }

  def publishLocalModule(ref: ProjectRef, state: State): Unit = {
    println("Publishing " + ref)
    evaluateTask(Keys.publishLocal in configuration, ref, state)
  }

  def evaluateTask[A](key: TaskKey[A], ref: ProjectRef, state: State): Option[(sbt.State, sbt.Result[A])] = {
    EvaluateTask(Project.extract(state).structure, key, state, ref, EvaluateTask defaultConfig state)
  }

}

