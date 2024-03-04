package com.example.extensions

import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Routing.userRoutes() {
    get("/user") {
      call.respondText("User1")
    }
    post("/user") {
      call.respondText("The user has been created")
    }
}