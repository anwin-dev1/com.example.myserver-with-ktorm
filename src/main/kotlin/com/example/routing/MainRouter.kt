package com.example.routing


import com.example.routing.Route.authenticationRouter
import com.example.routing.Route.notesRouter
import com.example.routing.Route.testApi
import com.example.routing.Route.userAccount
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Application.mainRouter(db:Database){

    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    routing {
        route("api/"){
            authenticationRouter(db,tokenManager)
            notesRouter(db)
            testApi()
            userAccount(db)
        }
    }
}