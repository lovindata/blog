package confs

case class EnvConf() {
  private val allEnvVar: Map[String, String] = sys.env

  val devMode: Boolean = allEnvVar.getOrElse("TARP_DEV_MODE", default = "true") == "true"
  val port: Int        = allEnvVar.getOrElse("TARP_PORT", default = "8080").toInt

  val postgresIp: String       = allEnvVar.getOrElse("TARP_POSTGRES_IP", default = "localhost")
  val postgresPort: Int        = allEnvVar.getOrElse("TARP_POSTGRES_PORT", default = "5432").toInt
  val postgresDb: String       = allEnvVar.getOrElse("TARP_POSTGRES_DB", default = "tarp")
  val postgresUser: String     = allEnvVar.getOrElse("TARP_POSTGRES_USER", default = "tarp")
  val postgresPassword: String = allEnvVar.getOrElse("TARP_POSTGRES_PASSWORD", default = "tarp")
  val postgresSchema: String   = allEnvVar.getOrElse("TARP_POSTGRES_SCHEMA", default = "tarp")
}

object EnvConf { implicit val impl: EnvConf = EnvConf() }
