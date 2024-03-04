package com.example.homework.chapter6


fun main() {

//  for (i  in 5..5000 step(5)) {
//    println(i)
//  }

//  for (i in -500..0) {
//    println(i)
//  }

  var total = 0
  var secondToLast = 0
  var last = 1

  for (i in 0..13) {
    total = secondToLast + last
    secondToLast = last
    last = total
    println(total)
  }


  iloop@ for (i in 1..5) {
    println(i)
    if (i==2) break
    for (j in 11..20) {
      println(j)
      if (j == 11) break
      for (k in 100..90) {
        println(k)
        if (k == 98) continue@iloop
      }
    }
  }

  var num = 300
  var dnum = when {
   num > 100 -> -234.567
   num < 100 -> 444.555
    else-> 0
  }

  println(dnum)

}