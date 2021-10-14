package com.androiddevs.routes

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.exercise.*
import com.androiddevs.data.responses.SimpleResponse
import com.noteapp.database.collections.Exercise
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.exerciseRoutes() {

    route("/getExercises") {
        authenticate {
            get {
                val request = try {
                    call.receive<GetExercisesRequests>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@get
                }
                val exercise = getExercises(request.parent)
                call.respond(OK, exercise)
            }
        }
    }


    route("/createExercise") {
        authenticate {
            post {
                val exercise = try {
                    call.receive<Exercise>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (createExercises(exercise)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/updateExercise") {
        authenticate {
            post {
                val exercise = try {
                    call.receive<Exercise>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (updateExercises(exercise)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/getExercisesSharedWIthMe") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val sharedExerciseWIthMe = getExercisesSharedWIthMe(email)
                call.respond(OK, sharedExerciseWIthMe)
            }
        }
    }

    route("/shareExerciseWithOthers") {
        authenticate {
            post {
                val request = try {
                    call.receive<ShareExerciseWithOthersRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (!checkIfUserExists(request.email)) {
                    call.respond(OK, SimpleResponse(false, "The entered user does not exist!"))
                    return@post
                }
                if (isExerciseOwner(request.exerciseId, request.owner)) {
                    call.respond(OK, SimpleResponse(false, "This user is already an owner of this exercise"))
                    return@post
                }
                if (isExerciseAlreadyShared(request.exerciseId, request.email)) {
                    call.respond(OK, SimpleResponse(false, "This exercise is already shared"))
                    return@post
                }
                if (shareExerciseWithOthers(request.exerciseId, request.email)) {
                    call.respond(OK, SimpleResponse(true, "${request.email} can now see this exercise"))
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/removeExercisesFromSharedWithMeList") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<RemoveExerciseFromSharedWithMeListRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (removeExerciseFromSharedWithMeList(request.exerciseId, request.email)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/getFavoriteExercises") {
        authenticate {
            get {
                val parent = call.principal<UserIdPrincipal>()!!.name

                val favoriteExercises = getFavoriteExercises(parent)
                call.respond(OK, favoriteExercises)
            }
        }
    }

    route("/addExerciseToFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddExerciseToFavoriteRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (addExerciseToFavorite(request.exerciseId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/removeExerciseFromFavorite") {
        authenticate {
            post {
                val request = try {
                    call.receive<RemoveExerciseFromFavoriteRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (removeExerciseFromFavorite(request.exerciseId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteExercise") {
        authenticate {
            post {
                val request = try {
                    call.receive<DeleteExerciseRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (deleteExercise(request.exerciseId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

}