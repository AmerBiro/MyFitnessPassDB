package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.noteapp.database.collections.Info
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.infoRoutes() {

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

    route("/getInfo") {
        authenticate {
            get {
                val parent = call.principal<UserIdPrincipal>()!!.name
                val info = getInfo(parent)
                call.respond(OK, info)
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