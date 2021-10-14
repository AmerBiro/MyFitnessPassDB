package com.androiddevs.routes.program

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.program.*
import com.androiddevs.data.responses.SimpleResponse
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
                val parent = call.principal<UserIdPrincipal>()!!.name
                val programs = getPrograms(parent)
                call.respond(OK, programs)
            }
        }
    }

    route("/createProgram"){
        authenticate {
            post {
                val program = try {
                    call.receive<Program>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createProgram(program)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/updateProgram"){
        authenticate {
            post {
                val program = try {
                    call.receive<Program>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (updateProgram(program)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/getProgramsSharedWIthMe") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val sharedProgramsWIthMe = getProgramsSharedWIthMe(email)
                call.respond(OK, sharedProgramsWIthMe)
            }
        }
    }

    route("/shareProgramWithOthers") {
        authenticate {
            post {
                val request = try {
                    call.receive<ShareProgramWithOthersRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (!checkIfUserExists(request.email)){
                    call.respond(OK, SimpleResponse(false, "The entered user does not exist!"))
                    return@post
                }
                if (isProgramOwner(request.programId, request.owner)){
                    call.respond(OK, SimpleResponse(false, "This user is already an owner of this program"))
                    return@post
                }
                if (isProgramAlreadyShared(request.programId, request.email)){
                    call.respond(OK, SimpleResponse(false, "This program is already shared"))
                    return@post
                }
                if (shareProgramWithOthers(request.programId, request.email)){
                    call.respond(OK, SimpleResponse(true, "${request.email} can now see this program"))
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/removeProgramFromSharedWithMeList") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<RemoveProgramFromSharedWithMeListRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(removeProgramFromSharedWithMeList(request.programId, request.email)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/getFavoritePrograms") {
        authenticate {
            get {
                val parent = call.principal<UserIdPrincipal>()!!.name

                val favoritePrograms = getFavoritePrograms(parent)
                call.respond(OK, favoritePrograms)
            }
        }
    }

    route("/addProgramToFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddProgramToFavoriteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(addProgramToFavorite(request.programId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/removeProgramFromFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<RemoveProgramFromFavoriteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(removeProgramFromFavorite(request.programId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteProgram") {
        authenticate {
            post {
                val request = try {
                    call.receive<DeleteProgramRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteProgram(request.programId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

}