package com.androiddevs.routes

import com.androiddevs.data.collections.ProgramNote
import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.program_note.DeleteProgramNoteRequest
import com.androiddevs.data.requests.program_note.GetProgramsNoteRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.programNotesRoutes() {

    route("/getProgramsNotes") {
        authenticate {
            get {
                val owner = try {
                    call.receive<GetProgramsNoteRequest>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@get
                }
                val programsNotes = getOwnProgramsNotes(owner.programId)
                call.respond(OK, programsNotes)
            }
        }
    }

    route("/createProgramsNotes"){
        authenticate {
            post {
                val programsNotes = try {
                    call.receive<ProgramNote>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createProgramNotes(programsNotes)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/updateProgramsNotes"){
        authenticate {
            post {
                val programsNotes = try {
                    call.receive<ProgramNote>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (updateProgramNotes(programsNotes)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteProgramsNotes") {
        authenticate {
            post {
                val programsNotes = try {
                    call.receive<DeleteProgramNoteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteProgramNote(programsNotes.programNoteId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

}