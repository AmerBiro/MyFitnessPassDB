package com.androiddevs.routes

import com.androiddevs.data.collections.User
import com.androiddevs.data.queries.checkIfUserExists
import com.androiddevs.data.queries.checkPasswordForEmail
import com.androiddevs.data.queries.registerUser
import com.androiddevs.data.requests.user.UserRequest
import com.androiddevs.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.userRoute(){

    route("/register") {
        post {
            val request = try {
                call.receive<UserRequest>()
            } catch(e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userExists = checkIfUserExists(request.email)
            if(!userExists) {
                if(registerUser(User(request.email, request.password))) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Account created successfully"))
                } else {
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "Error during creating account"))
                }
            } else {
                call.respond(HttpStatusCode.OK, SimpleResponse(false, "This email already exists"))
            }
        }
    }

    route("/login") {
        post {
            val request = try {
                call.receive<UserRequest>()
            } catch(e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.email, request.password)
            if(isPasswordCorrect) {
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Logged in successfully"))
            } else {
                call.respond(HttpStatusCode.OK, SimpleResponse(false, "Invalid email or password"))
            }
        }
    }

}