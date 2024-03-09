package com.lovindata
package features.pet.dto

sealed trait PetDto // An ADT

object PetDto {
  case class Dog(name: String, age: Int)  extends PetDto
  case class Cat(name: String, age: Int)  extends PetDto
  case class Fish(name: String, age: Int) extends PetDto
  case class Bird(name: String, age: Int) extends PetDto
}
