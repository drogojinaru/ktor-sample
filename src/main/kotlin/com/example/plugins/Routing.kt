package com.example.plugins

import com.example.Book
import com.example.books
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
    }
  }
  routing {
    books()

    get("/") {
      call.respondText("Hello World!")
    }

    get("/library/book/{bookId}/checkout") {
      val bookId = call.parameters["bookId"] ?: "no_id"
      call.respondText("you checked out the: $bookId")
    }

    get("/library/book/{bookId}/reserve") {
      val bookId = call.parameters["bookId"] ?: "no_id"
      call.respond(BookReserveResponse("tou reserved the book id: $bookId", emptyList()))
    }

    get("/library/book/{bookId}") {
      val bookId = call.parameters["bookId"] ?: "no_id"
      val book = Book(bookId, "How to grow Apple", "Mr. Appleton")
      val hypermediaLinks = listOf<HypermediaLinks>(
        HypermediaLinks("http://localhost:8080/library/book/$bookId/checkout", "checkout", "GET"),
        HypermediaLinks("http://localhost:8080/library/book/$bookId/reserve", "reserve", "GET")
      )
      val bookResponse = BookResponse(book, hypermediaLinks)
      call.respond(bookResponse)
    }
  }
}

data class BookResponse(val book: Book, val links: List<HypermediaLinks>)
data class HypermediaLinks(val href: String, val rel: String, val type: String)
data class BookReserveResponse(val message: String, val links: List<HypermediaLinks>)