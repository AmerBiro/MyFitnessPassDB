package com.noteapp.routes

import com.noteapp.database.queries.checkPasswordForEmail
import com.noteapp.database.requests.AccountRequest
import com.noteapp.database.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.HttpStatusCode.Companion.OK

fun Route.loginRoute(){
    route("/login"){
        post {
            val request = try {
                call.receive<AccountRequest>()
            }catch (e: ContentTransformationException){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.email, request.password)
            if (isPasswordCorrect){
                call.respond(OK, SimpleResponse(true, "Successfully logged in"))
            }else{
                call.respond(OK, SimpleResponse(false, "Invalid email or password"))
            }
        }
    }
}