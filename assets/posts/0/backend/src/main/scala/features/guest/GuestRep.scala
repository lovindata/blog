package com.lovindata
package features.guest

import cats.effect._
import cats.effect.unsafe.implicits._
import features.guest.dto.GuestDto

object GuestRep { // This layer is not important. It's an in-memory table for the example to work.
  def insert(dto: GuestDto): IO[GuestMod] = guestsTable.modify { table =>
    val id    = table.length
    val guest = GuestMod.buildFromDto(id, dto)
    (table :+ guest, guest) // (Updated table, Returned class)
  }

  def list(): IO[Vector[GuestMod]] = guestsTable.get

  private val guestsTable: Ref[IO, Vector[GuestMod]] =
    Ref[IO].of(Vector.empty[GuestMod]).unsafeRunSync() // A concurrent safe in memory table
}
