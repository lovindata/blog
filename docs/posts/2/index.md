---
title: "Introducing TARP Stack ⛺ – Tapir, React and PostgreSQL"
date: 2024-04-18
categories:
  - TypeScript
  - Scala
  - Docker
  - Frontend
  - Backend
  - DevOps
---

I landed my first job as a Data Engineer using [Scala](https://www.scala-lang.org/). It's been over 3 years now, approaching 4 years. The more experience you gain, the more you want to spread your wings 🪽 to tackle even bigger and more complex projects than just data pipelines, like developing **full-stack web data applications**. But, I really do not want to dissipate myself too much on all the programming languages, libraries, or frameworks out there 😣. These are just tools. What's important is how efficiently you can use them for the product or feature you envisioned 🦄🌈. Sooo! For me, it's currently the **TARP** tech stack!

<!-- more -->

![TARP](image.png)

## 🤔 What is TARP?

**TARP** stands for **Tapir**, **React** and **PostgreSQL**. In detail:

- 🦛 [**Tapir**](https://tapir.softwaremill.com/en/latest/): For the backend, it's a lightweight library similar to [FastAPI](https://fastapi.tiangolo.com/), designed for building endpoints and providing free SwaggerUI docs.
- ⚛️ [**React**](https://react.dev/): For the frontend, it's the most popular framework with the largest community, according to the [Stack Overflow Developer Survey 2023](https://survey.stackoverflow.co/2023/#section-admired-and-desired-web-frameworks-and-technologies).
- 🐘 [**PostgreSQL**](https://www.postgresql.org/): Chosen for the database due to its popularity and strong community support, as indicated by the [Stack Overflow Developer Survey 2023](https://survey.stackoverflow.co/2023/#section-admired-and-desired-web-frameworks-and-technologies).

I'm really excited 😄 to demonstrate how productive you can be with this tech stack. Let's start building, shall we! By the way, do you know what "TARP" stands for? 🤣 A tarp functions as a waterproof protective cover, for example, when building tents ⛺.

## 👨‍💻 Development Environment

For your coding environment, I highly recommend using [VSCode](https://code.visualstudio.com/). It has amazing support for [TypeScript](https://www.typescriptlang.org/) and [Docker](https://www.docker.com/) with various extensions. Scala development can also be done on VSCode using the [Metals](https://scalameta.org/metals/docs/editors/vscode/) extension. I used to develop on IntelliJ, but got tired of switching between VSCode and [IntelliJ](https://www.jetbrains.com/idea/) 😫. So yeah, if you have to handle more than just Scala code, just go with VSCode 😋.

Let's create 3 folders: `./devops`, `./backend`, `./frontend`, and also the `./vscode.code-workspace` file.

```json title="./vscode.code-workspace"
{
  "folders": [
    {
      "path": "backend"
    },
    {
      "path": "devops"
    },
    {
      "path": "frontend"
    }
  ]
}
```

You've just organized your project into [VSCode workspaces](https://code.visualstudio.com/docs/editor/workspaces). This is a way to instruct VSCode to treat each folder as an independent workspace, allowing you to **work on them simultaneously within a single VSCode window**. When you open the `./vscode.code-workspace` file using VSCode, it will automatically detect three workspaces.

<figure markdown="span">
  ![VSCode workspaces](image-4.png)
  <figcaption>VSCode workspaces</figcaption>
</figure>

The goal of this section is to establish a well-structured VSCode folder for your FullStack application as shown below 👇.

<figure markdown="span">
  ![Folder structure](image-1.png)
  <figcaption>Folder structure</figcaption>
</figure>

Let's start! 😤

### Database

The goal here is to set up a local PostgreSQL database and be able to explore it with suitable tools. This will be achieved using a [PostgreSQL Docker container](https://hub.docker.com/_/postgres) and the SQLTools VSCode extension. Please install:

- 🐳 [Docker Desktop](https://www.docker.com/products/docker-desktop/): For setting up a local PostgreSQL database.
- ➕ [Docker](https://code.visualstudio.com/docs/containers/overview) VSCode extension: To execute Docker commands directly via the VSCode UI.
- 🔎 [SQLTools](https://marketplace.visualstudio.com/items?itemName=mtxr.sqltools) and [SQLTools PostgreSQL/Cockroach Driver](https://marketplace.visualstudio.com/items?itemName=mtxr.sqltools-driver-pg) VSCode extensions: For viewing the local PostgreSQL database.

Let's create a [Docker Compose](https://docs.docker.com/compose/gettingstarted/) file, which is simply a YAML file with specific syntax, to run the local PostgreSQL database.

```yaml title="./devops/dev-local/docker-compose.yml"
services:
  database:
    image: postgres:16.2
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_PASSWORD=tarp
      - POSTGRES_USER=tarp
      - POSTGRES_DB=tarp
    volumes:
      - ./data:/var/lib/postgresql/data # Optional, but can keep our database data persistent on the host disk.
```

You are now all set to run it:

- Right click on `devops/dev-local/docker-compose.yml`
- Click on `Compose Up`

After a little while, if you go to the Docker Desktop application, you should see your local PostgreSQL database running 😃!

<figure markdown="span">
  ![PostgreSQL container](image-2.png)
  <figcaption>PostgreSQL container</figcaption>
</figure>

To ensure the local PostgreSQL setup is correct, you can explore it using the SQLTools extension in VSCode. To do this, add a new connection in SQLTools:

- `CTRL + SHIFT + P`
- Click on `SQLTools Management: Add New Connection`
- Follow the instructions and fill in the fields according to how the PostgreSQL container is defined in `devops/dev-local/docker-compose.yml`

<figure markdown="span">
  ![PostgreSQL view using SQLTools](image-3.png)
  <figcaption>PostgreSQL view using SQLTools</figcaption>
</figure>

If you've reached this point, your local PostgreSQL database is now all set for development! 👍

### Backend

Let's set up our Scala project! First, install:

- ☕️ [Java 17](https://adoptium.net/temurin/releases/?version=17): Because Scala runs on top of the JVM.
- ⚙️ [Metals](https://scalameta.org/metals/docs/editors/vscode/) VSCode extension: For supporting us during implementation in Scala.

Then, two files must be defined:

- `./backend/project/build.properties`: For specifying the [SBT](https://www.scala-sbt.org/) (the Scala dependencies manager) version.

```properties title="./backend/project/build.properties"
# https://github.com/sbt/sbt (look tags for version)
sbt.version=1.9.9
```

- `./backend/build.sbt`: For project metadata and dependencies.

```scala title="./backend/build.sbt"
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

/**
 * Test dependencies.
 */
// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest"          % "3.2.18"  % Test
// https://mvnrepository.com/artifact/org.mockito/mockito-scala
libraryDependencies += "org.mockito"   %% "mockito-scala"      % "1.17.31" % Test
// https://mvnrepository.com/artifact/org.mockito/mockito-scala-cats
libraryDependencies += "org.mockito"   %% "mockito-scala-cats" % "1.17.31" % Test
```

To instruct Metals to set up the Scala environment and install the dependencies:

- `CTRL + SHIFT + P`
- `Metals: Import build`

Then, go to the Metals extension view to confirm it's correctly set up 😎:

<figure markdown="span">
  ![Metals extension](image-5.png)
  <figcaption>Metals extension</figcaption>
</figure>

!!! warning

    [Metals uses VSCode workspace setup](https://scalameta.org/metals/blog/2023/07/17/workspace-folders) to detect Scala projects. So, it's important to have correctly set up VSCode workspaces as explained previously!

We're also going to initialize the backend API and database driver.

- First, set up the environement variables 🔑.

```scala title="./backend/src/main/scala/confs/EnvConf.scala"
package confs

case class EnvConf() {
  private val allEnvVar: Map[String, String] = sys.env

  val devMode: Boolean =
    allEnvVar.getOrElse("TARP_DEV_MODE",
                        default = "true") == "true" // To handle different behaviors in dev and prod environments
  val port: Int = allEnvVar.getOrElse("TARP_PORT", default = "8080").toInt

  val postgresIp: String       = allEnvVar.getOrElse("TARP_POSTGRES_IP", default = "localhost")
  val postgresPort: Int        = allEnvVar.getOrElse("TARP_POSTGRES_PORT", default = "5432").toInt
  val postgresDb: String       = allEnvVar.getOrElse("TARP_POSTGRES_DB", default = "tarp")
  val postgresUser: String     = allEnvVar.getOrElse("TARP_POSTGRES_USER", default = "tarp")
  val postgresPassword: String = allEnvVar.getOrElse("TARP_POSTGRES_PASSWORD", default = "tarp")
  val postgresSchema: String   = allEnvVar.getOrElse("TARP_POSTGRES_SCHEMA", default = "tarp")
}

object EnvConf { implicit val impl: EnvConf = EnvConf() }
```

- Second, integrate 🦛 Tapir using [http4s](https://http4s.org/v0.23/docs/quickstart.html) under the hood.

```scala title="./backend/src/main/scala/confs/ApiConf.scala"
package confs

import cats.effect.IO
import cats.implicits._
import com.comcast.ip4s.IpLiteralSyntax
import com.comcast.ip4s.Port
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.{Logger => LoggerMiddleware}
import org.http4s.server.middleware.CORS
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.LoggerInterpolator
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

case class ApiConf()(implicit envConf: EnvConf, logger: Logger[IO] = Slf4jLogger.getLogger) {
  def setup: IO[Unit] = for {
    port      <-
      IO.fromOption(Port.fromInt(envConf.port))(new RuntimeException(s"Not processable port number ${envConf.port}."))
    corsPolicy = CORS.policy.withAllowOriginHostCi(_ =>
                   envConf.devMode) // Essential for local development setup with an SPA running on a separate port
    _         <- EmberServerBuilder
                   .default[IO]
                   .withHost(ipv4"0.0.0.0")                    // Accept connections from any available network interface
                   .withPort(port)                             // On a given port
                   .withHttpApp(corsPolicy(allRts).orNotFound) // Link all routes to the backend server
                   .build
                   .use(_ => IO.never)
                   .start
                   .void
  } yield ()

  private val docsEpt =
    SwaggerInterpreter().fromEndpoints[IO](List.empty, "Backend – TARP Stack ⛺", "1.0")
  private val allRts  = {
    val loggerMiddleware =
      LoggerMiddleware.httpRoutes(                 // To log incoming requests or outgoing responses from the server
        logHeaders = true,
        logBody = true,
        redactHeadersWhen = _ => !envConf.devMode, // Display header values exclusively during development mode
        logAction = Some((msg: String) => info"$msg")
      )(_)
    loggerMiddleware(Http4sServerInterpreter[IO]().toRoutes(docsEpt))
  }
}

object ApiConf { implicit val impl: ApiConf = ApiConf() }
```

- Lastly, integrate the database driver. Utilize a combination of [Doobie](https://tpolecat.github.io/doobie/) for interacting with our database and ♻️ [Flyway](https://github.com/flyway/flyway) to manage the database schema lifecycle changes.

```scala title="./backend/src/main/scala/confs/DbConf.scala"
package confs

import cats.effect.IO
import cats.effect.Resource
import doobie.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway

case class DbConf()(implicit envConf: EnvConf) {
  def setup: IO[Unit] = {
    def raiseUnlessDbUp: IO[Unit] = for {
      isUp <- run(sql"SELECT 1;".query[Long].unique.map(_ == 1L))
      _    <- IO.raiseUnless(isUp)(new RuntimeException(s"Postgres ${envConf.postgresDb} database is down."))
    } yield ()
    def migrate: IO[Unit]         = IO.blocking {
      Flyway.configure
        .dataSource(
          s"jdbc:postgresql://${envConf.postgresIp}:${envConf.postgresPort}/${envConf.postgresDb}?currentSchema=${envConf.postgresSchema}",
          envConf.postgresUser,
          envConf.postgresPassword
        )
        .group(true)
        .table("flyway")                 // ⚠️ "flyway" as migration table history (in 'currentSchema' see above)
        .locations("conf/DbConf/flyway") // ".sql" files migration resource path
        .failOnMissingLocations(true)
        .load
        .migrate                         // Auto create schema if not exists & Rollback raise exception if failed
    }

    for {
      _ <- raiseUnlessDbUp
      _ <- migrate
    } yield ()
  }

  def run[A](sqls: ConnectionIO[A]): IO[A] = transactor.use(sqls.transact[IO])

  private val transactor: Resource[IO, HikariTransactor[IO]] = for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32)
    xa <- HikariTransactor.newHikariTransactor[IO](
            "org.postgresql.Driver",
            s"jdbc:postgresql://${envConf.postgresIp}:${envConf.postgresPort}/${envConf.postgresDb}?currentSchema=${envConf.postgresSchema}",
            envConf.postgresUser,
            envConf.postgresPassword,
            ce
          )
  } yield xa
}

object DbConf { implicit val impl: DbConf = DbConf() }
```

- Finally, create a Main class entry point to enable all these components to run.

```scala title="./backend/src/main/scala/Main.scala"
import cats.effect.IO
import cats.effect.IOApp
import confs.ApiConf
import confs.DbConf

object Main extends IOApp.Simple {
  override def run: IO[Unit] = DbConf.impl.setup >> ApiConf.impl.setup >> IO.never
}
```

In VSCode, after your code has compiled, you should see a `Run` ▶️ button appear above your `./backend/src/main/scala/Main.scala` class. Click 👆 on it, then go to [http://localhost:8080/docs/](http://localhost:8080/docs/) to see if it worked!

<figure markdown="span">
  ![SwaggerUI](image-6.png)
  <figcaption>SwaggerUI</figcaption>
</figure>

[SwaggerUI](https://swagger.io/tools/swagger-ui/) is accessible for documenting your endpoints for the frontend team. Currently, there are none, but that will change in the second section when we begin building application logic. Let's proceed with setting up the frontend development environment.

### Frontend

To begin, we need to install [Node.js](https://nodejs.org/en). It's necessary for running [npm](https://www.npmjs.com/) commands and installing dependencies. One of the first dependencies we need is a build tool for frontend SPAs. There are quite a few out there, but for me, [Vite](https://vitejs.dev/) is the best and fastest when it comes to building SPAs with React. To create a React project using Vite:

- Look at your VSCode navigation bar at the top > Click on `Terminal` > Click on `frontend` > A terminal for `./frontend` should appear
- Then run `npm init vite@latest` and follow the instructions

For the instructions, make sure to:

- Create your project at `./frontend`
- Use `React` and not be baited by `Preact` 😂
- Select `TypeScript + SWC` for TypeScript compilation, which includes a [Rust-based](https://www.rust-lang.org/) engine for SPEEEEEEEED! 💨

Files should magically appear in your `./frontend` VSCode workspace! 🌈 Now, let's install the dependencies defined in `./frontend/package.json`. To do that, run the following command:

```bash
npm install
```

You should see a folder `./node_modules` appear, where all the dependencies will now reside. From this point, you can run the predefined application that Vite gave you when we initialized the project:

```bash
npm run dev
```

Then, navigate to [http://localhost:5173/](http://localhost:5173/).

<figure markdown="span">
  ![Vite + React page](image-7.png)
  <figcaption>Vite + React page</figcaption>
</figure>

Congratulations, your frontend development environment has been successfully set up ✅. The development team behind Vite has truly done an amazing job to make it so easy! 😌

## 🏗️ Building Your Application

### Database

### Backend

### Frontend

## 🎁 Wrapping Up For Production

### Serving React via Tapir

### Optimized Docker Build

## 🌟 Full-Stack Developer Reached!
