package com.ilovedatajjia
package config

object EnvLoaderConf {
  private val allEnvVar: Map[String, String] = sys.env

  val backendPort: Int = allEnvVar.getOrElse("BACKEND_PORT", default = "8080").toInt
}
