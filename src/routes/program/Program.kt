package com.androiddevs.routes.program

import com.androiddevs.data.queries.getProgramsForUser
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.programRoutes() {
    route("/getPrograms") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val notes = getProgramsForUser(email)
                call.respond(OK, notes)
            }
        }
    }
}