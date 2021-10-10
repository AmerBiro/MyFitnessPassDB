package com.noteapp

import com.noteapp.database.collections.User
import com.noteapp.database.queries.registerUser
import com.noteapp.database.routes.registerRoute
import com.noteapp.routes.loginRoute
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // default header which contains useful info about the requests to the server such as date
    install(DefaultHeaders)
    // have a log over HTTP requests
    install(CallLogging)
    install(Routing){
        registerRoute()
        loginRoute()
    }
    // make the server respindes witha a certain content
    install(ContentNegotiation) {
        // JSON response and requests from server
        gson {
            setPrettyPrinting()
        }
    }


}

