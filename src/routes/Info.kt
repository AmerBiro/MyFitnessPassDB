package com.androiddevs.routes

import com.androiddevs.data.queries.createUpdateInfo
import com.androiddevs.data.queries.createUpdateProgram
import com.androiddevs.data.queries.getInfo
import com.androiddevs.data.queries.getPrograms
import com.noteapp.database.collections.Info
import com.noteapp.database.collections.Program
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.infoRoutes() {
    route("/getInfo") {
        authenticate {
            get {
                val userId = call.principal<UserIdPrincipal>()!!.name

                val info = getInfo(userId)
                call.respond(HttpStatusCode.OK, info)
            }
        }
    }

    route("/createUpdateInfo"){
        authenticate {
            post {
                val info = try {
                    call.receive<Info>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createUpdateInfo(info)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}