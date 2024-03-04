package com.example

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.books() {

  val dataManager = DataManager()

  route("/books") {
    get {
      call.respond(dataManager.allBooks())
    }
    post("/{id}") {
      val id = call.parameters["id"]
      println("post id: $id")
      val book = call.receive<Book>()
      val updatedBook = dataManager.updateBook(book)
      call.respondText { "The book has been updated: $updatedBook" }

    }

    put {
      val book = call.receive<Book>()
      val newBook = dataManager.newBook(book)
      call.respond("The book has been updated: $newBook")
    }

    delete("/{id}") {
      val id = call.parameters["id"].toString()
      println("delete id: $id")
      val deletedBook = dataManager.deleteBook(id)
      call.respond(deletedBook!!)
    }
  }
}