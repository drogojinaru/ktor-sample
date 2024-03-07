package com.example.routes

import com.example.ui.data.Book
import com.example.ui.data.DataManagerMongoDB
import com.example.ui.Endpoints
import com.example.ui.book.BookTemplate
import io.ktor.http.content.PartData
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlinx.html.i
import org.slf4j.LoggerFactory

@Location("/api/book/list")
data class BookListLocation(val sortby: String, val asc: Boolean)

fun Route.books() {

  post(Endpoints.BOOK_SEARCH.url) {
    val log = LoggerFactory.getLogger("LoginView")
    val multipart = call.receiveMultipart()
    var search = ""
    while (true) {
      val part = multipart.readPart() ?: break
      if (part is PartData.FormItem) {
        log.info("FormItem: ${part.name} = ${part.value}")
        if (part.name == "search") search = part.value
      }
      part.dispose()
    }
    val session = call.sessions.get<Session>()!!
    DataManagerMongoDB.cartForUser(session)?.let {
      call.respondHtmlTemplate(BookTemplate(it, DataManagerMongoDB.searchBooks(search))) {
        searchFilter {
          i { +"Search filter: $search" }
        }
      }
    }
  }
  get(Endpoints.BOOKS.url){
    call.respondHtmlTemplate(BookTemplate(null, DataManagerMongoDB.allBooks())){}
  }
  route("/books") {
    get {
      call.respond(DataManagerMongoDB.allBooks())
    }
  authenticate("bookstoreAuth") {
    get<BookListLocation> {
      call.respond(DataManagerMongoDB.sortedBooks(it.sortby, it.asc))
    }

      post("/{id}") {
        val id = call.parameters["id"]
        println("post id: $id")
        val book = call.receive<Book>()
        val updatedBook = DataManagerMongoDB.updateBook(book)
        call.respondText { "The book has been updated: $updatedBook" }

      }

      put {
        val book = call.receive<Book>()
        val newBook = DataManagerMongoDB.newBook(book)
        call.respond("The book has been updated: $newBook")
      }

      delete("/{id}") {
        val id = call.parameters["id"].toString()
        println("delete id: $id")
        val deletedBook = DataManagerMongoDB.deleteBook(id)
        call.respond(deletedBook!!)
      }
    }
  }
}