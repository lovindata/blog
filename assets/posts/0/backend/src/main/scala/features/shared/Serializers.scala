package com.lovindata
package features.shared

import features.guest.GuestMod.GenderEnum
import features.guest.GuestMod.GenderEnum.Gender
import io.circe._
import io.circe.generic.extras.AutoDerivation // ⚠️ Switched!
import io.circe.generic.extras.Configuration
import sttp.tapir.Schema
import sttp.tapir.generic.{Configuration => TapirConfiguration}
import sttp.tapir.generic.auto.SchemaDerivation

object Serializers extends AutoDerivation with SchemaDerivation { // HERE! ✨ Auto Derivation Magic!
  // The 2 implicits 👇
  implicit val encDecConf: Configuration   = Configuration.default.withDiscriminator("type")
  implicit val schConf: TapirConfiguration = TapirConfiguration.default.withDiscriminator("type")

  // Enumeration needs to be auto derived manually with theses 3 lines 👇 (It will use enumeration actual values when (en/de)coding)
  implicit val genderEnc: Encoder[Gender] = Encoder.encodeEnumeration(GenderEnum)
  implicit val genderDec: Decoder[Gender] = Decoder.decodeEnumeration(GenderEnum)
  implicit val genderSch: Schema[Gender]  = Schema.derivedEnumerationValue[Gender]
}
