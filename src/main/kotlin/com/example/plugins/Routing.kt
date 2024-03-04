package com.example.plugins

import com.example.Book
import com.example.DataManager
import com.example.books
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.statuspages.statusFile
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File
import org.slf4j.LoggerFactory

fun Application.configureRouting() {

  val log = LoggerFactory.getLogger(Application::class.java)


  install(StatusPages) {
    statusFile(
      HttpStatusCode.InternalServerError,
      HttpStatusCode.NotFound,
      filePattern = "customerrors/myerror#.html"
      )

    exception<MyFirstException> { call, cause ->
      call.respond(HttpStatusCode.Unauthorized)
      log.error(cause.localizedMessage)
      throw cause
    }

    exception<MySecondException> { call, cause ->
      call.respondRedirect("/")
      log.error(cause.localizedMessage)
      throw cause
    }

    exception<MyThirdException> { call, cause ->
      call.respondText("the third error happened, fix that")
      log.error(cause.localizedMessage)
      throw cause
    }

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

    get("/first") {
      throw MyFirstException()
    }
    get("/second") {
      throw MySecondException()
    }
    get("/third") {
      throw MyThirdException()
    }

    post("/text") {
      val text = call.receiveText()
      println("text received: $text")
      call.respondText("thank you, we received text: $text")
    }

    post("/form") {
      val params = call.receiveParameters()
      params.names().forEach {
        val value = params[it]
        println("key: $it, value: $value")
      }
      call.respondText("thank you for the form data")
    }

    post("/file") {
      val multipart = call.receiveMultipart()
      var title = ""
      val uploadDir = "./upload"
      multipart.forEachPart { part ->
        when (part) {
          is PartData.FormItem -> {
            if (part.name == "title") {
              title = part.value
            }
          }
          is PartData.FileItem -> {
            val ext = part.originalFileName?.let { it1 -> File(it1).extension }
            val file = File(uploadDir, "upload-${System.currentTimeMillis()}-${title.hashCode()}.$ext")
            part.streamProvider().use { input ->
              file.outputStream().buffered().use { output -> input.copyTo(output) }
            }
          }
          else -> {
            log.info("unexpected formData part")
          }
        }
        part.dispose()
      }
    }
  }
}

data class BookResponse(val book: Book, val links: List<HypermediaLinks>)
data class HypermediaLinks(val href: String, val rel: String, val type: String)
data class BookReserveResponse(val message: String, val links: List<HypermediaLinks>)

class MyFirstException: RuntimeException()
class MySecondException: RuntimeException()
class MyThirdException: RuntimeException()