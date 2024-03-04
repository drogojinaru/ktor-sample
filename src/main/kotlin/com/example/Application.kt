package com.example

import com.example.extensions.userRoutes
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    .start(wait = true)
}

fun Application.module() {
  configureHTTP()
  configureSerialization()
  configureSecurity()
  configureTemplating()
  configureMonitoring()
  configureRouting()
  routing {
    userRoutes()
  }
}
