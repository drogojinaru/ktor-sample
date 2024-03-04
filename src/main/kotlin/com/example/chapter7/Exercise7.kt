package com.example.chapter7

fun main() {

  val joe = Person("Joe", "Jones", 45)
  val jane = Person("Jane", "Smith", 12)
  val mary = Person("Mary", "Wilson", 18)
  val john = Person("John", "Adams", 66)
  val jean = Person("Jean", "Smithson", 55)

  val people = mapOf(
    joe.lastname to joe,
    jane.lastname to jane,
    mary.lastname to mary,
    john.lastname to john,
    jean.lastname to jean
  ).also { it.map { u -> println("${u.value.firstname} id ${u.value.age} old") } }
  println(people.filter { it.value.lastname.startsWith("S") }.count())

  val pairs = people.map { Pair(it.value.firstname, it.value.lastname) }

  println(pairs)

  val (name, surname, age) = joe
  println("fName: $name, fSurname: $surname, age: $age")

  val list1 = listOf(1,4,9,15,33)
  val list2 = listOf(4, 15, 55, -83, 22, 101)

  val list3 = list1.filter { it in list2 }
  val list4 = list1.filter { list2.contains(it) }

  println("list3: $list3")
  println("list4: $list4")

  var regularPaper = Box<Regular>()
  var paper = Box<Paper>()

  paper = regularPaper
}

data class Person(val firstname: String, val lastname: String, val age: Int)

class Box<out T> {
  
}

open class Paper {

}

open class Regular: Paper() {

}

open class Premium: Paper() {

}