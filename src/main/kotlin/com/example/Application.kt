package com.example

import com.example.plugins.configureHTTP
import com.example.plugins.configureLocation
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.plugins.configureTemplating
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    .start(wait = true)
}

fun Application.module() {
  configureLocation()
  configureHTTP()
  configureSerialization()
  configureSecurity()
  configureTemplating()
  configureMonitoring()
  configureRouting()
}
