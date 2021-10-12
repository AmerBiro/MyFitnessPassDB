package com.androiddevs.routes.program

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.*
import com.noteapp.database.collections.Program
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.client.engine.*
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
    route("/getOwnPrograms") {
        authenticate {
            get {
                val request = try {
                    call.receive<GetOwnProgramsRequests>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@get
                }
                val ownPrograms = getOwnPrograms(request.owner)
                call.respond(OK, ownPrograms)
            }
        }
    }

    route("/getProgramsSharedWIthMe") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val sharedProgramsWIthMe = getSharedProgramsWIthMe(email)
                call.respond(OK, sharedProgramsWIthMe)
            }
        }
    }

//    route("/shareProgramWithOthers"){
//        authenticate {
//            post {
//                val shareProgram = try {
//                    call.receive<ShareProgramWithOthersRequest>()
//                }catch (e: ContentTransformationException){
//                    call.respond(BadRequest)
//                    return@post
//                }
//                if (shareProgramWithOthers(shareProgram.programId, shareProgram.email)){
//                    call.respond(OK)
//                }else{
//                    call.respond(Conflict)
//                }
//            }
//        }
//    }


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

    route("/deleteProgram") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteProgramRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteProgram(request.owner, request.programId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }


}