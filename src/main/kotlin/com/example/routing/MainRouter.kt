package com.example.routing


import com.example.routing.Route.notesRouter
import com.example.routing.Route.testApi
import com.example.routing.Route.userAccount
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Application.mainRouter(db:Database){
    routing {
        route("api/"){
            authenticationRouter(db)
            notesRouter(db)
            testApi()
            userAccount(db)
        }
    }
}