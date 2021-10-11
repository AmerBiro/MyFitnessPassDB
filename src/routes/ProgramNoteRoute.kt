package com.androiddevs.routes

import com.androiddevs.data.collections.ProgramNote
import com.androiddevs.data.queries.createUpdateProgram
import com.androiddevs.data.queries.createUpdateProgramNotes
import com.androiddevs.data.queries.getProgramNotes
import com.androiddevs.data.queries.getPrograms
import com.noteapp.database.collections.Program
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.programNotesRoutes() {
    route("/getProgramNotes") {
        authenticate {
            get {
                val programId = call.principal<UserIdPrincipal>()!!.name

                val programNotes = getProgramNotes(programId)
                call.respond(HttpStatusCode.OK, programNotes)
            }
        }
    }

    route("/createUpdateProgramNotes"){
        authenticate {
            post {
                val programNotes = try {
                    call.receive<ProgramNote>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createUpdateProgramNotes(programNotes)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}