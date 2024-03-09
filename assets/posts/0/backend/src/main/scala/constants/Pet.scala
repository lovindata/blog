package com.lovindata
package constants

sealed trait Pet // It's an ADT because "Product types" and "Sum types"
object Pet {
  case class Dog(name: String,
                 age: Int) // 👈 "Product types" because "(String, Int)" == More complex type by combining types
      extends Pet // "Sum types" because "Dog != Cat != Fish != Bird but Dog + Cat + Fish + Bird = Pet" == Distinct types when united define the type
  case class Cat(name: String, age: Int)  extends Pet
  case class Fish(name: String, age: Int) extends Pet
  case class Bird(name: String, age: Int) extends Pet

  val pets: Vector[Pet] = Vector(
    Dog(name = "Max", age = 3),
    Fish(name = "Poissy (← RER A \uD83E\uDD2A, don't worry if you don't understand, Parisians will! XDD)", age = 2),
    Dog(name = "Doggy", age = 1)
  )
}
