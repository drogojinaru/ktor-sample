package com.example.routes

import com.example.checkout.ReceiptTemplate
import com.example.ui.Endpoints
import com.example.ui.data.DataManagerMongoDB
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions

fun Route.receiptRouting() {
  get(Endpoints.RECEIPT.url) {
    val session = call.sessions.get<Session>()!!
    call.respondHtmlTemplate(ReceiptTemplate(DataManagerMongoDB.cartForUser(session))) {}
  }
}