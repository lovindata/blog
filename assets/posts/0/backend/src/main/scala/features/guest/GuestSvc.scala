package com.ilovedatajjia
package features.guest

import cats.effect.IO
import features.guest.dto.GuestDto
import shared.BackendException.BadRequestException

object GuestSvc {
  def letEnterAdultGuest(dto: GuestDto): IO[GuestMod] = for {
    _     <- IO.raiseUnless(dto.age >= 18)(
               BadRequestException("You are not an adult!") // Exception of "BadRequestException" raised
             )
    guest <- GuestRep.insert(dto)
  } yield guest

  def listGuests(): IO[Vector[GuestMod]] = GuestRep.list()
}
