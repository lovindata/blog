/**
 * Project configurations.
 */
ThisBuild / scalaVersion := "2.13.13" // https://www.scala-lang.org/download/all.html
lazy val root = (project in file(".")).settings(name := "backend")

/**
 * Dev dependencies.
 */
// Cats & Scala extensions
// https://mvnrepository.com/artifact/org.typelevel/cats-effect
libraryDependencies += "org.typelevel" %% "cats-effect"        % "3.5.4"
// https://github.com/oleg-py/better-monadic-for (look tags for version)
addCompilerPlugin("com.olegpy"         %% "better-monadic-for" % "0.3.1")
// https://mvnrepository.com/artifact/dev.optics/monocle-core
libraryDependencies += "dev.optics"    %% "monocle-core"       % "3.2.0"
// https://mvnrepository.com/artifact/dev.optics/monocle-macro
libraryDependencies += "dev.optics"    %% "monocle-macro"      % "3.2.0"

// Tapir
// https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-http4s-server
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"     % "1.10.4"
// https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-json-circe
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % "1.10.4"
// https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-swagger-ui-bundle
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.10.4"

// Http4s
// https://mvnrepository.com/artifact/org.http4s/http4s-ember-server
libraryDependencies += "org.http4s" %% "http4s-ember-server" % "0.23.26"
// https://mvnrepository.com/artifact/org.http4s/http4s-circe
libraryDependencies += "org.http4s" %% "http4s-circe"        % "0.23.26"
// https://mvnrepository.com/artifact/org.http4s/http4s-dsl
libraryDependencies += "org.http4s" %% "http4s-dsl"          % "0.23.26"
// https://mvnrepository.com/artifact/org.http4s/http4s-ember-client
libraryDependencies += "org.http4s" %% "http4s-ember-client" % "0.23.26"

// Log4j2
// https://mvnrepository.com/artifact/org.typelevel/log4cats-core
libraryDependencies += "org.typelevel" %% "log4cats-core" % "2.6.0"
// https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
libraryDependencies += "org.slf4j"      % "slf4j-simple"  % "2.0.13"

// Circe
// https://mvnrepository.com/artifact/io.circe/circe-parser
libraryDependencies += "io.circe" %% "circe-parser"         % "0.14.6"
// https://mvnrepository.com/artifact/io.circe/circe-generic
libraryDependencies += "io.circe" %% "circe-generic"        % "0.14.6"
// https://mvnrepository.com/artifact/io.circe/circe-generic-extras
libraryDependencies += "io.circe" %% "circe-generic-extras" % "0.14.3"
// https://mvnrepository.com/artifact/io.circe/circe-literal
libraryDependencies += "io.circe" %% "circe-literal"        % "0.14.6"

// Doobie
// https://mvnrepository.com/artifact/org.tpolecat/doobie-core
libraryDependencies += "org.tpolecat" %% "doobie-core"     % "1.0.0-RC5"
// https://mvnrepository.com/artifact/org.tpolecat/doobie-postgres
libraryDependencies += "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC5"
// https://mvnrepository.com/artifact/org.tpolecat/doobie-hikari
libraryDependencies += "org.tpolecat" %% "doobie-hikari"   % "1.0.0-RC5"

// Flyway
// https://mvnrepository.com/artifact/org.flywaydb/flyway-core
libraryDependencies += "org.flywaydb" % "flyway-core"                % "10.11.1"
// https://mvnrepository.com/artifact/org.flywaydb/flyway-database-postgresql
libraryDependencies += "org.flywaydb" % "flyway-database-postgresql" % "10.11.1" % "runtime"
