package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.partialcontent.PartialContent

fun Application.configureHTTP() {
  install(PartialContent) {
    // Maximum number of ranges that will be accepted from a HTTP request.
    // If the HTTP request specifies more ranges, they will all be merged into a single range.
    maxRangeCount = 10
  }
}
