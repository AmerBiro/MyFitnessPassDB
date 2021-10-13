package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.noteapp.database.collections.Info
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.infoRoutes() {
    route("/getOwnInfo") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val ownInfo = getOwnInfo(email)
                call.respond(HttpStatusCode.OK, ownInfo)
            }
        }
    }

    route("/createInfo"){
        authenticate {
            post {
                val info = try {
                    call.receive<Info>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createInfo(info)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
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
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (updateInfo(info)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }
}