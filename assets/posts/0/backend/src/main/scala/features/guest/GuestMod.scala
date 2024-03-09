package com.lovindata
package features.guest

import features.guest.GuestMod.GenderEnum.Gender
import features.guest.dto.GuestDto

case class GuestMod( // Returned by the endpoints == "Scala -> JSON" (also corresponds to an entity in table)
    id: Long,
    name: String,
    gender: Gender,  // A non Scala simple type that needs to be derived manually! (JSON <-> Scala)
    age: Int,
    job: String)

object GuestMod {
  def buildFromDto(id: Long, dto: GuestDto): GuestMod = GuestMod(id, dto.name, dto.gender, dto.age, dto.job)

  object GenderEnum extends Enumeration {
    type Gender = Value
    val Male: Value      = Value("male")
    val Female: Value    = Value("female")
    val NonBinary: Value = Value("non-binary")
  }
}
