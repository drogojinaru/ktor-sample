package com.example.homework

class Exercise1 {

  fun exercise1() {

    val hello1 = "Hello"
    val hello2 = "Hello"


    println("are those ref eq? : , ${ hello1 === hello2 }")
    println("are those struct eq? : , ${ hello1 == hello2 }")

    var int = 2988

    val any: Any = "The Any type is the root of Kotlin class hierarchy"
    if (any is String) {
      println(any.uppercase())
    }

    val theMostIndentedStringEverWritten = """    1
        |   11
        |  111
        | 1111
    """.trimMargin()

    println(theMostIndentedStringEverWritten)
  }

}