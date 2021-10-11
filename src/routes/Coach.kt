package com.androiddevs.routes

import com.androiddevs.data.queries.createUpdateCoach
import com.androiddevs.data.queries.createUpdateProgram
import com.androiddevs.data.queries.getCoach
import com.androiddevs.data.queries.getPrograms
import com.noteapp.database.collections.Coach
import com.noteapp.database.collections.Program
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.coachRoutes() {
    route("/getCoach") {
        authenticate {
            get {
                val userId = call.principal<UserIdPrincipal>()!!.name

                val coach = getCoach(userId)
                call.respond(HttpStatusCode.OK, coach)
            }
        }
    }

    route("/createUpdateCoach"){
        authenticate {
            post {
                val coach = try {
                    call.receive<Coach>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createUpdateCoach(coach)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}