package com.androiddevs.routes.program

import com.androiddevs.data.queries.createUpdateProgram
import com.androiddevs.data.queries.getPrograms
import com.noteapp.database.collections.Program
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.ContentTransformationException
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.programRoutes() {
    route("/getPrograms") {
        authenticate {
            get {
                val userId = call.principal<UserIdPrincipal>()!!.name

                val programs = getPrograms(userId)
                call.respond(OK, programs)
            }
        }
    }

    route("/createUpdateProgram"){
        authenticate {
            post {
                val program = try {
                    call.receive<Program>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createUpdateProgram(program)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

}