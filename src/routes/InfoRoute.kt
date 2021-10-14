package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.noteapp.database.collections.Info
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.infoRoutes() {
    route("/getOwnInfo") {
        authenticate {
            get {
                val parent = call.principal<UserIdPrincipal>()!!.name
                val info = getOwnInfo(parent)
                call.respond(OK, info)
            }
        }
    }

    route("/createInfo"){
        authenticate {
            post {
                val info = try {
                    call.receive<Info>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createInfo(info)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/updateInfo"){
        authenticate {
            post {
                val info = try {
                    call.receive<Info>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (updateInfo(info)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
}