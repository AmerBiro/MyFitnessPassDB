package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.program_day.DeleteProgramDayRequest
import com.androiddevs.data.requests.program_day.GetProgramsDayRequest
import com.androiddevs.data.requests.program_note.GetProgramsNoteRequest
import com.noteapp.database.collections.ProgramDay
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.programDayRoutes() {

    route("/getProgramDay") {
        authenticate {
            get {
                val request = try {
                    call.receive<GetProgramsDayRequest>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@get
                }
                val programDay = getProgramDay(request.programId)
                call.respond(OK, programDay)
            }
        }
    }

    route("/createProgramDay"){
        authenticate {
            post {
                val programDay = try {
                    call.receive<ProgramDay>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createProgramDay(programDay)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/updateProgramDay"){
        authenticate {
            post {
                val programDay = try {
                    call.receive<ProgramDay>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (updateProgramDay(programDay)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteProgramDay") {
        authenticate {
            post {
                val programDay = try {
                    call.receive<DeleteProgramDayRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteProgramDay(programDay.programDayId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

}