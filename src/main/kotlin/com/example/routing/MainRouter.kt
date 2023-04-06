package com.example.routing


import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Application.mainRouter(db:Database){
    routing {
        route("api/"){
            authenticationRouter(db)
            notesRouter(db)
            testApi()
        }
    }
}