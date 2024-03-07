package com.example.ui.login

import com.example.ui.GeneralViewTemplate
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.h2

class LogoutTemplate : Template<HTML> {
  private val basicTemplate = GeneralViewTemplate()
  override fun HTML.apply() {
    insert(basicTemplate) {
      content {
        div(classes = "mt-2") {
          h2 { +"You have been logged out!" }
        }
      }
    }
  }
}