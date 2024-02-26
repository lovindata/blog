package com.ilovedatajjia
package features.pet

import cats.effect.IO
import features.pet.dto.PetDto
import features.pet.dto.PetDto.Dog
import shared.BackendException.BadRequestException

object PetSvc {
  def petContest(dto: Vector[PetDto]): IO[PetDto] = for {
    _      <- IO.raiseUnless(dto.nonEmpty)(BadRequestException("Where are the pets ?! 😡"))
    pets    = dto.sortWith {
                case (pet0: Dog, pet1: Dog) => pet0.age < pet1.age // Sort before the younger dog
                case (_: Dog, _)            => true                // Sort before the dog
                case _                      => false
              }
    bestPet = pets.head
  } yield bestPet
}
