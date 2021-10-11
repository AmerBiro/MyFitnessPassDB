package com.androiddevs.routes

import com.androiddevs.data.queries.createUpdateSets
import com.androiddevs.data.queries.getSets
import com.noteapp.database.collections.Sets
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.setsRoutes() {
    route("/getSets") {
        authenticate {
            get {
                val exerciseId = call.principal<UserIdPrincipal>()!!.name

                val sets = getSets(exerciseId)
                call.respond(HttpStatusCode.OK, sets)
            }
        }
    }

    route("/createUpdateSets") {
        authenticate {
            post {
                val sets = try {
                    call.receive<Sets>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createUpdateSets(sets)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}
