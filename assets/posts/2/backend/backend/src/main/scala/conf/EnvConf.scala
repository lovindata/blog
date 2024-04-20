package com.lovindata
package conf

case class EnvConf() {
  private val allEnvVar: Map[String, String] = sys.env

  val backendDevMode: Boolean = allEnvVar.getOrElse("BACKEND_DEV_MODE", default = "true") == "true"
  val backendPort: Int        = allEnvVar.getOrElse("BACKEND_PORT", default = "8080").toInt

  val postgresIp: String       = allEnvVar.getOrElse("POSTGRES_IP", default = "localhost")
  val postgresPort: Int        = allEnvVar.getOrElse("POSTGRES_PORT", default = "5432").toInt
  val postgresDb: String       = allEnvVar.getOrElse("POSTGRES_DB", default = "tarp")
  val postgresUser: String     = allEnvVar.getOrElse("POSTGRES_USER", default = "tarp")
  val postgresPassword: String = allEnvVar.getOrElse("POSTGRES_PASSWORD", default = "tarp")
  val postgresSchema: String   = allEnvVar.getOrElse("POSTGRES_SCHEMA", default = "tarp")
}

object EnvConf { implicit val impl: EnvConf = EnvConf() }
