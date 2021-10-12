package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.coach.*
import com.androiddevs.data.requests.program.*
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
                val request = try {
                    call.receive<GetOwnCoachRequests>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val ownCoach = getOwnCoach(request.owner)
                call.respond(HttpStatusCode.OK, ownCoach)
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

    route("/shareCoachWithOthers") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<ShareCoachWithOthersRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(shareCoachWithOthers(request.programId, request.email)) {
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
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteCoachRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(deleteCoach(request.owner, request.programId)) {
                    call.respond(HttpStatusCode.OK)
                } else {
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
                if(removeCoachFromSharedWithMeList(request.programId, request.email)) {
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
                val request = try {
                    call.receive<GetFavoriteCoach>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val favoriteCoach = getFavoriteCoach(request.owner)
                call.respond(HttpStatusCode.OK, favoriteCoach)
            }
        }
    }

    route("/addCoachToFavorite") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<AddCoachToFavoriteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(addCoachToFavorite(request.programId)) {
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
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<RemoveCoachFromFavorite>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(removeCoachFromFavorite(request.programId)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}