package com.androiddevs.routes

import com.androiddevs.data.collections.ProgramNote
import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.GetProgramNoteRequest
import com.noteapp.database.collections.Program
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.programNotesRoutes() {
    route("/getProgramNotes") {
        authenticate {

            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<GetProgramNoteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@get
                }
                val programNotes = getProgramNotes(email, request.programId)
                call.respond(OK, programNotes)
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