/**
 * Project configurations.
 */
ThisBuild / scalaVersion := "2.13.14" // https://www.scala-lang.org/download/all.html
lazy val root = (project in file("."))
  .settings(
    name         := "scala-spark",
    artifactName := ((scalaVersion, _, artifact) =>
      s"${artifact.name}_${scalaVersion.binary}.${artifact.extension}") // Uses docker image for version
  )

/**
 * Dev dependencies.
 */
// Spark
// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.5.1" % "provided"
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql"  % "3.5.1" % "provided"

/**
 * Test dependencies.
 */
// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest"     % "3.2.18"  % Test
// https://mvnrepository.com/artifact/org.mockito/mockito-scala
libraryDependencies += "org.mockito"   %% "mockito-scala" % "1.17.31" % Test
