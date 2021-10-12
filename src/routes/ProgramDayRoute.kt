package com.androiddevs.routes

import com.androiddevs.data.queries.createUpdateProgramDay
import com.androiddevs.data.queries.getProgramDay
import com.noteapp.database.collections.ProgramDay
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.programDayRoutes() {
    route("/getProgramDay") {
        authenticate {
            get {
                val programDayId = call.principal<UserIdPrincipal>()!!.name

                val programDay = getProgramDay(programDayId)
                call.respond(HttpStatusCode.OK, programDay)
            }
        }
    }

    route("/createUpdateProgramDay"){
        authenticate {
            post {
                val programDay = try {
                    call.receive<ProgramDay>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createUpdateProgramDay(programDay)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}