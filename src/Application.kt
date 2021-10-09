package com.noteapp

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // default header which contains useful info about the requests to the server such as date
    install(DefaultHeaders)
    // have a log over HTTP requests
    install(CallLogging)
    install(Routing)
    // make the server respindes witha a certain content
    install(ContentNegotiation) {
        // JSON response and requests from server
        gson {
            setPrettyPrinting()
        }
    }



}

