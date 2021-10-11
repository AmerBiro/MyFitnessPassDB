package com.androiddevs.routes

import com.androiddevs.data.queries.createUpdateExercises
import com.androiddevs.data.queries.getExercises
import com.noteapp.database.collections.Exercise
import com.noteapp.database.collections.Program
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.exerciseRoutes() {
    route("/getExercises") {
        authenticate {
            get {
                val dayId = call.principal<UserIdPrincipal>()!!.name

                val exercises = getExercises(dayId)
                call.respond(HttpStatusCode.OK, exercises)
            }
        }
    }

    route("/createUpdateExercises"){
        authenticate {
            post {
                val exercise = try {
                    call.receive<Exercise>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createUpdateExercises(exercise)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}