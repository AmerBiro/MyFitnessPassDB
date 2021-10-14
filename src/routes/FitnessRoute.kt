package com.androiddevs.routes.fitness

import com.androiddevs.data.queries.*
import com.androiddevs.data.requests.fitness.DeleteFitnessRequest
import com.noteapp.database.collections.Fitness
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.fitnessRoutes() {

    route("/getFitness") {
        authenticate {
            get {
                val parent = call.principal<UserIdPrincipal>()!!.name
                val fitness = getFitness(parent)
                call.respond(OK, fitness)
            }
        }
    }

    route("/createFitness"){
        authenticate {
            post {
                val fitness = try {
                    call.receive<Fitness>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if (createFitness(fitness)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
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
                    call.respond(BadRequest)
                    return@post
                }
                if (updateFitness(fitness)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteFitness") {
        authenticate {
            post {
                val request = try {
                    call.receive<DeleteFitnessRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteFitness(request.fitnessId)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

}