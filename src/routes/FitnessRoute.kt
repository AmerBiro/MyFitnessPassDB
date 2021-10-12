package com.androiddevs.routes.fitness

import com.androiddevs.data.queries.createUpdateFitness
import com.androiddevs.data.queries.createUpdateProgram
import com.androiddevs.data.queries.getFitness
import com.noteapp.database.collections.Fitness
import com.noteapp.database.collections.Program
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

    route("/createUpdateFitness"){
        authenticate {
            post {
                val fitness = try {
                    call.receive<Fitness>()
                }catch (e: ContentTransformationException){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if (createUpdateFitness(fitness)){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }


}