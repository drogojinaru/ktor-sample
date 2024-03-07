package com.example.routes

import com.example.Constants
import com.example.ui.data.DataManagerMongoDB
import com.example.SecurityHandler
import com.example.ui.Endpoints
import com.example.ui.book.BookTemplate
import com.example.ui.home.HomeTemplate
import com.example.ui.login.LoginTemplate
import com.example.ui.login.LogoutTemplate
import io.ktor.http.content.PartData
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.sessions
import org.slf4j.LoggerFactory

data class Session(val username: String)

fun Route.loginView() {

  get(Endpoints.LOGIN.url) {
    call.respondHtmlTemplate(LoginTemplate()) {}
  }

  get(Endpoints.HOME.url) {
    call.respondHtmlTemplate(HomeTemplate()) {}
  }

  get(Endpoints.LOGOUT.url) {
    call.sessions.clear(Constants.COOKIE_NAME.value)
    call.respondHtmlTemplate(LogoutTemplate()) {}
  }

  post(Endpoints.DOLOGIN.url) {
    val log = LoggerFactory.getLogger("LoginView")
    val multipart = call.receiveMultipart()

    call.request.headers.forEach {s, list ->
      log.info("key $s values $list")
    }
    var username = ""
    var password = ""

    while (true) {
      val part = multipart.readPart() ?: break
      when (part) {
        is PartData.FormItem -> {
          log.info("FormItem: ${part.name} = ${part.value}")
          if (part.name == "username") username = part.value
          if (part.name == "password") password = part.value
        }
        is PartData.FileItem -> {
          log.info("FileItem: ${part.name} -> ${part.originalFileName} of ${part.contentType}")
        }
        else -> {
          log.info("Part item is unknown type")
        }
      }
      part.dispose()
    }
    if (SecurityHandler().isValid(username, password)) {
      val session = Session(username)
      call.sessions.set(Constants.COOKIE_NAME.value, Session(username))
      DataManagerMongoDB.cartForUser(session)?.let {
        call.respondHtmlTemplate(BookTemplate(it, DataManagerMongoDB.allBooks())) {
          searchFilter {
            +"You are logged in as $username and a cookie has been created"
          }
        }
      }

    } else {
      call.respondHtmlTemplate(LoginTemplate()) {
        greeting {
          + "username or password is invalid, please try again"
        }
      }
    }
  }
}