package com.example.plugins

import com.example.Constants
import com.example.routes.Session
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.basic
import io.ktor.server.auth.form
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

fun Application.configureSecurity() {
  install(Sessions) {
    cookie<Session>(Constants.COOKIE_NAME.value)
  }
  val users = listOf("shopper1", "shopper2", "shopper3")
  val admins = listOf("admin")
  authentication {
    basic(name = "bookstoreAuth") {
      realm = "Ktor Server"
      validate {
        if ((users.contains(it.name) || (admins.contains("it.name"))) && it.password == "password") {
          UserIdPrincipal(it.name)
        }
      else null }
    }

    form(name = "myauth2") {
      userParamName = "user"
      passwordParamName = "password"
      challenge {
        /**/
      }
    }
  }
  routing {
    authenticate("bookstoreAuth") {
      get("/api/tryauth") {
        val principal = call.principal<UserIdPrincipal>()!!
        call.respondText("Hello ${principal.name}")
      }
    }
    authenticate("myauth2") {
      get("/protected/route/form") {
        val principal = call.principal<UserIdPrincipal>()!!
        call.respondText("Hello ${principal.name}")
      }
    }
  }
}

class UserSession(accessToken: String)
