package academy.learnprogramming.oochallenge

import kotlinx.css.Color

open class Bicycle(var cadence: Int, var gear: Int = 10, var speed: Int) {
  fun applyBreak(decrement: Int) {
    speed -= decrement
  }
  fun speedUp(increment: Int) {
    speed += increment
  }

  open fun printDescription() {
    println("Bike is in gear: $gear, with a cadence of: $cadence, at the speed of: $speed")
  }
}

class MountainBike(cadence: Int, gear: Int = 10, speed: Int, var seatHeight: Int):
  Bicycle(cadence, gear, speed) {

  companion object {
    val colors = arrayOf("blue", "red", "white", "black", "green", "brown")
  }
  constructor(color: String, cadence: Int, gear: Int = 10, speed: Int, seatHeight: Int) :
      this(cadence, gear, speed, seatHeight) {
    println(color)
  }
    override fun printDescription() {
      super.printDescription()
      println("have additional seat height of MountainBike: $seatHeight")
    }
}

class RoadBike(cadence: Int, gear: Int = 10, speed: Int, val tireHeight: Int):
  Bicycle(cadence, gear, speed) {

  override fun printDescription() {
    super.printDescription()
    println("have additional seat height of RoadBike: $tireHeight")
  }
}

fun main() {
  val bike = Bicycle(1, 1, 10)
  val mountainBike = MountainBike(1, 1, 10, 16)
  val roadBike = RoadBike(1, 1, 10, 15)

  println(bike.printDescription())
  println(roadBike.printDescription())
  println(mountainBike.printDescription())

  val bikeOptional = Bicycle(cadence = 1, speed = 10)
  val mountainBikeOptional = MountainBike(cadence = 1, speed = 10, seatHeight = 16)
  val roadBikeOptional = RoadBike(cadence = 1, speed = 10, tireHeight = 15)

  println(bikeOptional.gear)
  println(mountainBikeOptional.gear)
  println(roadBikeOptional.gear)

  val iaraMountainBike = MountainBike("blue", 10, 10, 10, 10)
  println(iaraMountainBike.gear)

  MountainBike.colors.forEach { println(it) }

}
