package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.sets.DeleteSetRequest
import com.androiddevs.data.requests.sets.GetSetsDayRequest
import com.noteapp.database.collections.Sets
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.setsRoutes() {

    route("/getSets") {
        authenticate {
            get {
                val request = try {
                    call.receive<GetSetsDayRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@get
                }
                val sets = getSets(request.parent)
                call.respond(OK, sets)
            }
        }
    }

    route("/createSets"){
        authenticate {
            post {
                val sets = try {
                    call.receive<Sets>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createSets(sets)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/updateSets"){
        authenticate {
            post {
                val sets = try {
                    call.receive<Sets>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (updateSets(sets)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteSets") {
        authenticate {
            post {
                val request = try {
                    call.receive<DeleteSetRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteSets(request.setId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

}
