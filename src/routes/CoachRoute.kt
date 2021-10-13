package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.coach.*
import com.androiddevs.data.requests.program.*
import com.androiddevs.data.responses.SimpleResponse
import com.noteapp.database.collections.Coach
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.coachRoutes() {
    route("/getOwnCoach") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val ownCoach = getOwnCoach(email)
                call.respond(HttpStatusCode.OK, ownCoach)
            }
        }
    }

    route("/createCoach"){
        authenticate {
            post {
                val coach = try {
                    call.receive<Coach>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createCoach(coach)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

    route("/updateCoach"){
        authenticate {
            post {
                val coach = try {
                    call.receive<Coach>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (updateCoach(coach)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

    route("/getCoachSharedWIthMe") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val sharedCoachWIthMe = getCoachSharedWIthMe(email)
                call.respond(HttpStatusCode.OK, sharedCoachWIthMe)
            }
        }
    }



    route("/shareCoachWithOthers") {
        authenticate {
            post {
                val request = try {
                    call.receive<ShareCoachWithOthersRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (!checkIfUserExists(request.email)){
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "The entered user does not exist!"))
                    return@post
                }
                if (isOwnerCoach(request.coachId, request.email)){
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "This user is already an owner of this coach"))
                    return@post
                }
                if (isCoachAlreadyShared(request.coachId, request.email)){
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "This coach is already shared"))
                    return@post
                }
                if (shareCoachWithOthers(request.coachId, request.email)){
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "${request.email} can now see this program"))
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
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
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(removeCoachFromSharedWithMeList(request.coachId, request.email)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

    route("/getFavoriteCoach") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val favoriteCoach = getFavoriteCoach(email)
                call.respond(HttpStatusCode.OK, favoriteCoach)
            }
        }
    }

    route("/addCoachToFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddCoachToFavoriteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(addCoachToFavorite(request.coachId)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

    route("/removeCoachFromFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<RemoveCoachFromFavorite>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(removeCoachFromFavorite(request.coachId)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
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
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(deleteCoach(request.coachId)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}