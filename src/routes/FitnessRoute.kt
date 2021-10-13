package com.androiddevs.routes.fitness

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.fitness.DeleteFitness
import com.androiddevs.data.requests.program.*
import com.noteapp.database.collections.Fitness
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.fitnessRoutes() {

    route("/getFitness") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val fitness = getFitness(email)
                call.respond(HttpStatusCode.OK, fitness)
            }
        }
    }

    route("/createFitness"){
        authenticate {
            post {
                val fitness = try {
                    call.receive<Fitness>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createFitness(fitness)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

    route("/updateFitness"){
        authenticate {
            post {
                val fitness = try {
                    call.receive<Fitness>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (updateFitness(fitness)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

    route("/deleteFitness") {
        authenticate {
            post {
                val request = try {
                    call.receive<DeleteFitness>()
                } catch(e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(deleteFitness(request.fitnessId)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }

}