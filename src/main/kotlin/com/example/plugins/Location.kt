package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Locations

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureLocation() {
  install(Locations) {

  }
}