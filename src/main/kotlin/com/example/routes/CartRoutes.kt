package com.example.routes

import com.example.ui.Endpoints
import com.example.ui.book.BookTemplate
import com.example.ui.cart.CartTemplate
import com.example.ui.data.DataManagerMongoDB
import io.ktor.http.content.PartData
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlinx.html.i
import org.slf4j.LoggerFactory

fun Route.cartRouting() {

  get(Endpoints.CART.url) {
    val session = call.sessions.get<Session>()!!
    val cart = DataManagerMongoDB.cartForUser(session)
    call.respondHtmlTemplate(CartTemplate(cart)) {}
  }

  post(Endpoints.ADD_TO_CART.url) {
    val log = LoggerFactory.getLogger("Add to cart")
    val multipart = call.receiveMultipart()
    val session = call.sessions.get<Session>()!!
    var bookId = ""
    while (true) {
      val part = multipart.readPart() ?: break
      if (part is PartData.FormItem) {
        log.info("FormItem: ${part.name} = ${part.value}")
        if (part.name == "bookid") bookId = part.value
      }
      part.dispose()
    }
    DataManagerMongoDB.getBookWithId(bookId)?.let {
      DataManagerMongoDB.addBook(session, it)
    }
    DataManagerMongoDB.cartForUser(session)?.let {
      call.respondHtmlTemplate(BookTemplate(it, DataManagerMongoDB.allBooks())) {
        searchFilter {
          i { +"Book added to cart" }
        }
      }
    }
  }

  post(Endpoints.REMOVE_FROM_CART.url) {
    val log = LoggerFactory.getLogger("Remove from cart")
    val multipart = call.receiveMultipart()
    val session = call.sessions.get<Session>()!!
    var bookId = ""
    while (true) {
      val part = multipart.readPart() ?: break
      if (part is PartData.FormItem) {
        log.info("FormItem: ${part.name} = ${part.value}")
        if (part.name == "bookid") bookId = part.value
      }
      part.dispose()
    }
    DataManagerMongoDB.getBookWithId(bookId)?.let {
      DataManagerMongoDB.removeBook(session, it)
    }
    val cart = DataManagerMongoDB.cartForUser(session)
    call.respondHtmlTemplate(CartTemplate(cart)) {}
  }
}