---
title: Effortlessly Build Scala Rest APIs with Tapir, http4s, and Circe!
date: 2024-02-21
categories:
  - Scala
  - Backend
---

What if I told you there exists in Scala a way to build Rest APIs and generate SwaggerUI docs simultaneously? Moreover, it's as fast as using FastAPI (for those familiar with Python), while retaining the strong type safety and functional programming style of Scala! 😲 In this post, we'll explore this exciting tech stack: Tapir, http4s, and Circe!

<!-- more -->

![Tech stack](image.png)

## 😎 Why Tapir, http4s, Circe?

Let's check out what is each library:

- Tapir: A powerful Scala library for defining and documenting HTTP APIs in a type-safe and functional manner, with built-in support for generating Swagger UI documentation.
- http4s: A lightweight, purely functional Scala library for building HTTP servers and clients, designed for high performance and composability.
- Circe: A popular JSON library for Scala that provides seamless JSON parsing and encoding using a powerful and idiomatic functional approach.

<figure markdown="span">
  ![Handling Requests](image-1.png)
  <figcaption>Handling Requests</figcaption>
</figure>

As of now, in the Scala ecosystem, there are no actively maintained, production-ready alternatives to Tapir. However, alternatives to http4s include Play and Akka/Pekko Http, with Play being more a full-stack web framework. For JSON handling, alternatives to Circe include json4s, ZIO-json, and Play JSON. According to the [2023 Scala survey](https://scalasurvey2023.virtuslab.com/), it appears that the community is leaning towards the Typelevel ecosystem when it comes to building backend systems. You can also check library comparison websites like [LibHunt](https://scala.libhunt.com/) for further insights.

<figure markdown="span">
  ![Akka/Pekko ecosystem VS Typelevel ecosystem](image-2.png)
  <figcaption>Akka/Pekko ecosystem VS Typelevel ecosystem</figcaption>
</figure>

Either way, all these alternatives are production-ready and actively maintained. You cannot go wrong with choosing any of them.

## 🏰 Let's Build Our Backend Castle!

Setting up a Scala project is essential, and in our case, we'll do it using IntelliJ. However, you can also opt for VSCode with Metals. If it is your first time setting up a Scala project, you can follow Scala's official tutorial ["Getting Started with Scala in IntelliJ"](https://docs.scala-lang.org/getting-started/intellij-track/getting-started-with-scala-in-intellij.html). The folder structure should be as follows:

```
backend/
├── project/
│   ├── build.properties
│   └── plugins.sbt
├── src/
│   └── main/
│       ├── resources/
│       └── scala/
│           └── Main.scala
└── build.sbt
```

In your `build.sbt`, please add the following dependencies:

```scala
/**
 * Dev dependencies.
 */
// Cats & Scala extensions
// https://mvnrepository.com/artifact/org.typelevel/cats-effect
libraryDependencies += "org.typelevel" %% "cats-effect"        % "3.5.3"
// https://github.com/oleg-py/better-monadic-for (look tags for version)
addCompilerPlugin("com.olegpy"         %% "better-monadic-for" % "0.3.1")
// https://mvnrepository.com/artifact/dev.optics/monocle-core
libraryDependencies += "dev.optics"    %% "monocle-core"       % "3.2.0"

// Tapir
// https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-http4s-server
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"     % "1.9.10"
// https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-json-circe
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % "1.9.10"
// https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-swagger-ui-bundle
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.9.10"

// Http4s
// https://mvnrepository.com/artifact/org.http4s/http4s-ember-server
libraryDependencies += "org.http4s" %% "http4s-ember-server" % "0.23.25"
// https://mvnrepository.com/artifact/org.http4s/http4s-circe
libraryDependencies += "org.http4s" %% "http4s-circe"        % "0.23.25"
// https://mvnrepository.com/artifact/org.http4s/http4s-dsl
libraryDependencies += "org.http4s" %% "http4s-dsl"          % "0.23.25"
// https://mvnrepository.com/artifact/org.http4s/http4s-ember-client
libraryDependencies += "org.http4s" %% "http4s-ember-client" % "0.23.25"

// Circe
// https://mvnrepository.com/artifact/io.circe/circe-parser
libraryDependencies += "io.circe" %% "circe-parser"         % "0.14.6"
// https://mvnrepository.com/artifact/io.circe/circe-generic
libraryDependencies += "io.circe" %% "circe-generic"        % "0.14.6"
// https://mvnrepository.com/artifact/io.circe/circe-generic-extras
libraryDependencies += "io.circe" %% "circe-generic-extras" % "0.14.3"
// https://mvnrepository.com/artifact/io.circe/circe-literal
libraryDependencies += "io.circe" %% "circe-literal"        % "0.14.6"
```

You can run `Main.scala` by clicking on `▷` and selecting `Run 'Main'`. If it works, you are all set! 🥳

### ⚡ Backend Electricity Setup

Let's build our first electric central that will empower the future bridges:

<figure markdown="span">
  ![Backend Server Configuration](image-3.png)
  <figcaption>Backend Server Configuration</figcaption>
</figure>

First setup our service which loads environment variables and our backend exceptions:

```scala title="EnvLoaderConf.scala"
package com.ilovedatajjia
package config

object EnvLoaderConf {
  private val allEnvVar: Map[String, String] = sys.env

  val backendPort: Int = allEnvVar.getOrElse("BACKEND_PORT", default = "8080").toInt
}
```

```scala title="BackendException.scala"
package com.ilovedatajjia
package shared

sealed trait BackendException extends Exception

object BackendException {
  case class BadRequestException(message: String)          extends BackendException
  case class ServerInternalErrorException(message: String) extends BackendException
}
```

Second setup our backend server:

```scala title="BackendServerConf.scala"
package com.ilovedatajjia
package config

import cats.effect.IO
import com.comcast.ip4s._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.CORS
import shared.BackendException.ServerInternalErrorException

object BackendServerConf {
  def start: IO[Unit] = for {
    port <- IO.fromOption(Port.fromInt(envLoaderConf.backendPort))(
              ServerInternalErrorException(s"Not processable port number ${envLoaderConf.backendPort}."))
    _    <- EmberServerBuilder
              .default[IO]
              .withHost(ipv4"0.0.0.0") // Accept connections from any available network interface
              .withPort(port)          // On port 8080
              .build
              .use(_ => IO.never)
              .start
              .void
  } yield ()
}
```

Let's finally build the switch to turn on the electricity! 💡

```scala title="Main.scala"
package com.ilovedatajjia

import cats.effect._
import config.BackendServerConf

object Main extends IOApp.Simple {
  override def run: IO[Unit] = BackendServerConf.start >> IO.never // == non-terminating
}
```

If you visit [http://localhost:8080](http://localhost:8080) and see the message `Not found`, then congratulations! You've built your electric central! 🥳

### 🌉 First Bridge Endpoint!

It's now the exciting part because we are gonna connect to the external world with bridges! 🤩

<figure markdown="span">
  ![Backend Endpoint](image-4.png)
  <figcaption>Backend Endpoint</figcaption>
</figure>

First, implement the business logic, which in our case involves counting the number of characters from a text.

```scala title="TextSvc.scala"
package com.ilovedatajjia
package features.text

import cats.effect.IO
import cats.implicits._

object TextSvc {
  def countCharacters(text: String): IO[Int] = text.length.pure[IO]
}
```

Second, build the bridge! 🌉⚒️

```scala title="TextCtrl.scala"
package com.ilovedatajjia
package features.text

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter

object TextCtrl {
  def endpoints: List[AnyEndpoint] = List(countCharactersEpt)
  def routes: HttpRoutes[IO]       = countCharactersRts

  private val countCharactersEpt = endpoint // The endpoint and it is used to generate the OpenAPI doc
    .summary("Count characters")
    .get
    .in("count-characters" / query[String]("text"))
    .out(jsonBody[Int])
  private val countCharactersRts =          // It converts the endpoint to actual http4s route :O
    Http4sServerInterpreter[IO]().toRoutes(countCharactersEpt.serverLogicSuccess(text => TextSvc.countCharacters(text)))
}
```

Lastly, connect our new, fresh 🌉 bridge to our electrical central ⚡️—you know, the one we built in the previous part. 🤗

```scala title="BackendServerConf.scala" hl_lines="22 29 30 31 32"
package com.ilovedatajjia
package config

import cats.effect.IO
import cats.implicits._
import com.comcast.ip4s._
import features.text.TextCtrl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import shared.BackendException.ServerInternalErrorException
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object BackendServerConf {
  def start: IO[Unit] = for {
    port <- IO.fromOption(Port.fromInt(EnvLoaderConf.backendPort))(
              ServerInternalErrorException(s"Not processable port number ${EnvLoaderConf.backendPort}."))
    _    <- EmberServerBuilder
              .default[IO]
              .withHost(ipv4"0.0.0.0")        // Accept connections from any available network interface
              .withPort(port)                 // On port 8080
              .withHttpApp(allRts.orNotFound) // Link all routes to the backend server
              .build
              .use(_ => IO.never)
              .start
              .void
  } yield ()

  private val docsEpt = // Merge all endpoints as a fully usable OpenAPI doc
    SwaggerInterpreter().fromEndpoints[IO](TextCtrl.endpoints, "Backend", "1.0")
  private val allRts  = // Serve the OpenAPI doc & all the other routes
    Http4sServerInterpreter[IO]().toRoutes(docsEpt) <+> TextCtrl.routes
}
```

If you visit [http://localhost:8080/docs](http://localhost:8080/docs) and end up on a SwaggerUI page, then congratulations! You've built your first bridge! 🥳 You can play a little bit with your endpoint! 😄

<figure markdown="span">
  ![SwaggerUI](image-5.png)
  <figcaption>SwaggerUI</figcaption>
</figure>

## 🎨 Mastering Circe & Tapir

### ✨ Auto Derivation Magic!

### 🧙‍♂️ Crack ADTs!

## 🌟 Happy Endings!
