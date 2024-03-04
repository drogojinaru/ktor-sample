package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.auth.ldap.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.css.form

fun Application.configureSecurity() {
//  authentication {
//    oauth("auth-oauth-google") {
//      urlProvider = { "http://localhost:8080/callback" }
//      providerLookup = {
//        OAuthServerSettings.OAuth2ServerSettings(
//          name = "google",
//          authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
//          accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
//          requestMethod = HttpMethod.Post,
//          clientId = System.getenv("GOOGLE_CLIENT_ID"),
//          clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
//          defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
//        )
//      }
//      client = HttpClient(Apache)
//    }
//  }
//  val localhost = "http://0.0.0.0"
//  val ldapServerPort = 6998 // TODO: change to real value!
//  authentication {
//    basic("authName") {
//      realm = "realm"
//      validate { credential ->
//        ldapAuthenticate(credential, "ldap://$localhost:${ldapServerPort}", "uid=%s,ou=system")
//      }
//    }
//  }
//  // Please read the jwt property from the config file if you are using EngineMain
//  val jwtAudience = "jwt-audience"
//  val jwtDomain = "https://jwt-provider-domain/"
//  val jwtRealm = "ktor sample app"
//  val jwtSecret = "secret"
//  authentication {
//    jwt {
//      realm = jwtRealm
//      verifier(
//        JWT
//          .require(Algorithm.HMAC256(jwtSecret))
//          .withAudience(jwtAudience)
//          .withIssuer(jwtDomain)
//          .build()
//      )
//      validate { credential ->
//        if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//      }
//    }
//  }
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
//    authenticate("auth-oauth-google") {
//      get("login") {
//        call.respondRedirect("/callback")
//      }
//
//      get("/callback") {
//        val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
//        call.sessions.set(UserSession(principal?.accessToken.toString()))
//        call.respondRedirect("/hello")
//      }
//    }
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
