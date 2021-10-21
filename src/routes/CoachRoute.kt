package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.coach.*
import com.androiddevs.data.responses.SimpleResponse
import com.noteapp.database.collections.Coach
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.coachRoutes() {

    route("/createCoach"){
        authenticate {
            post {
                val coach = try {
                    call.receive<Coach>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createCoach(coach)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/getCoaches") {
        authenticate {
            get {
                val parent = call.principal<UserIdPrincipal>()!!.name
                val getCoaches = getCoaches(parent)
                call.respond(OK, getCoaches)
            }
        }
    }

    route("/updateCoach"){
        authenticate {
            post {
                val coach = try {
                    call.receive<Coach>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (updateCoach(coach)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/shareCoachWithOthers") {
        authenticate {
            post {
                val request = try {
                    call.receive<ShareCoachWithOthersRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (!checkIfUserExists(request.email)){
                    call.respond(OK, SimpleResponse(false, "The entered user does not exist!"))
                    return@post
                }
                if (isCoachOwner(request.coachId, request.owner)){
                    call.respond(OK, SimpleResponse(false, "This user is already an owner of this coach"))
                    return@post
                }
                if (isCoachAlreadyShared(request.coachId, request.email)){
                    call.respond(OK, SimpleResponse(false, "This coach is already shared"))
                    return@post
                }
                if (shareCoachWithOthers(request.coachId, request.email)){
                    call.respond(OK, SimpleResponse(true, "${request.email} can now see this program"))
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/getCoachesSharedWIthMe") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val sharedCoachWIthMe = getCoachesSharedWIthMe(email)
                call.respond(OK, sharedCoachWIthMe)
            }
        }
    }

    route("/removeCoachFromSharedWithMeList") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<RemoveCoachFromSharedWithMeListRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(removeCoachFromSharedWithMeList(request.coachId, request.email)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/addCoachToFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddCoachToFavoriteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(addCoachToFavorite(request.coachId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/getFavoriteCoaches") {
        authenticate {
            get {
                val parent = call.principal<UserIdPrincipal>()!!.name

                val favoriteCoach = getFavoriteCoach(parent)
                call.respond(OK, favoriteCoach)
            }
        }
    }

    route("/removeCoachFromFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<RemoveCoachFromFavoriteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(removeCoachFromFavorite(request.coachId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteCoach") {
        authenticate {
            post {
                val request = try {
                    call.receive<DeleteCoachRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteCoach(request.coachId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

}