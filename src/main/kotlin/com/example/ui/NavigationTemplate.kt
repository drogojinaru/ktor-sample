package com.example.ui

import com.example.ui.data.Cart
import io.ktor.server.html.PlaceholderList
import io.ktor.server.html.Template
import io.ktor.server.html.each
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.UL
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.span
import kotlinx.html.ul

class NavigationTemplate(private val cart: Cart?) : Template<FlowContent> {

  val menuItems = PlaceholderList<UL, FlowContent>()

  override fun FlowContent.apply() {
    div {
      nav(classes = "navbar navbar-expand-md navbar-dark bg-dark") {
        a(classes = "navbar-brand", href = "#") { +"My Bookstore" }
        button(classes = "navbar-toggler", type = ButtonType.button) {
          attributes["data-toggle"] = "collapse"
          attributes["data-target"] = "#navbarsExampleDefault"
          attributes["aria-controls"] = "navbarsExampleDefault"
          attributes["aria-expanded"] = "false"
          attributes["aria-label"] = "Toggle navigation"
          span(classes = "navbar-toggler-icon")
        }
        div(classes = "collapse navbar-collapse") {
          id = "navbarsExampleDefault"
          ul(classes = "navbar-nav mr-auto") {
            each(menuItems) {
              li {
                insert(it)
              }
            }
          }
        }
        div {
          cart?.let {
            form(action = Endpoints.CART.url) {
              button(classes = "btn btn-danger", type = ButtonType.submit) {
                +"Items in cart: ${it.qtyTotal}, total price: ${it.sum}"
              }
            }
          }
        }
      }
    }
  }
}