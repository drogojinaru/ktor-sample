package com.example.ui.book

import com.example.routes.Session
import com.example.ui.data.Book
import com.example.ui.Endpoints
import com.example.ui.GeneralViewTemplate
import com.example.ui.data.Cart
import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr

class BookTemplate(cart: Cart?, private val books: List<Book>, private val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(cart)) : Template<HTML> {

  val searchFilter = Placeholder<FlowContent>()
  override fun HTML.apply() {
    insert(basicTemplate) {
      content {
        div(classes = "mt-2") {
          h2 { +"Books available" }
          div { insert(searchFilter) }
          form(
            method = FormMethod.post,
            encType = FormEncType.multipartFormData,
            action = Endpoints.BOOK_SEARCH.url
          ) {
            div(classes = "row mb-3 mt-3") {
              div(classes = "md-6") {
                input(type = InputType.text, classes = "form-control", name = "search") {
                  placeholder = "Search for book"
                  attributes["aria-label"] = "Search"
                  attributes["aria-describedby"] = "basic-addon1"
                }
              }
              div(classes = "md-5 offset-md-1") {
                button(classes = "btn btn-primary", type = ButtonType.submit) {
                  +"Search"
                }
              }
            }
          }

          table(classes = "table table-striped") {
            thead {
              tr {
                th(scope = kotlinx.html.ThScope.col) { +"Id" }
                th(scope = kotlinx.html.ThScope.col) { +"Title" }
                th(scope = kotlinx.html.ThScope.col) { +"Author" }
                th(scope = kotlinx.html.ThScope.col) { +"Price" }
                th(scope = kotlinx.html.ThScope.col) { +"" }
              }
            }
            tbody {
              books.forEach {
                tr {
                  td { +it.id!! }
                  td { +it.title }
                  td { +it.author }
                  td { +"${it.price}" }
                  td {
                    form(
                      method = FormMethod.post,
                      encType = FormEncType.multipartFormData,
                      action = Endpoints.ADD_TO_CART.url
                    ) {
                      button(classes = "btn btn-success", type = kotlinx.html.ButtonType.submit) {
                        +"Add to cart"
                      }
                      input(type = InputType.hidden, name = "bookid") {
                        value = it.id!!
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}