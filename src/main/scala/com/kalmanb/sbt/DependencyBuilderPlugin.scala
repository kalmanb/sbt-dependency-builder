package com.kalmanb.sbt

import sbt.Keys._
import sbt.Load.BuildStructure
import sbt._

object DependencyBuilderPlugin extends Plugin {

  val kalKey = TaskKey[Unit]("kal")
  val dependencyBuilderSettings = Seq[Setting[_]](
    kalKey <<= (thisProjectRef, buildStructure, state) map {
      (thisProjectRef, structure, state) ⇒

        val missingDependencies: Seq[ModuleID] = getMissingDependencies(thisProjectRef, state)

        val modulesToBuild = for {
          ref ← structure.allProjectRefs
          if (missingDependencies.exists(d ⇒ d.name startsWith ref.project))
        } yield ref

        modulesToBuild foreach (publishLocalModule(_, state))

        evaluateTask(Keys.update in configuration, thisProjectRef, state)
    }
  )

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

