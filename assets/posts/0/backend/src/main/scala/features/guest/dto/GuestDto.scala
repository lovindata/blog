package com.lovindata
package features.guest.dto

import features.guest.GuestMod.GenderEnum.Gender

case class GuestDto( // It corresponds to the input of the endpoint (JSON -> Scala)
    name: String,
    gender: Gender,
    age: Int,
    job: String)
