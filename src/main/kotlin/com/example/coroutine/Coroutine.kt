package com.example.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() = runBlocking {
  withContext(Dispatchers.IO) {
    repeat(100_000) {
      launch {
        firstCoroutine(it)
      }
      println("Out of launch context")
    }
  }
  println("not in scope of coroutine")
}

suspend fun firstCoroutine(id: Int) {
  delay(1000)
  println("coroutine launched = $id")
}