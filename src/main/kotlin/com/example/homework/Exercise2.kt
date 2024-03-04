package com.example.homework

class Exercise2 {

  fun exercise2() {

    val float1: Float? = -3847.384f
    val float2: Float? = (-3847.384).toFloat()

    val shortArray = shortArrayOf(1,2,3,4,5)

    val shortArray100 = Array<Int?>(40) { i -> (i + 1) * 5 }

    val charArray = charArrayOf('a', 'b', 'c')

    val x: String? = "I AM AN UPPERCASE"

    val z = x?.lowercase() ?: "I give up"
    print(z)


    val y = x?.lowercase()?.replace("am", "am not")
    print(y)

    val myNotNullVal: Int? = 4
    myNotNullVal!!.toDouble()
  }
}