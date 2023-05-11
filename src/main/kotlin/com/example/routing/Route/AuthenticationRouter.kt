package com.example.routing.Route


import com.example.route.signUpAuth
import com.example.route.signinAuth
import com.example.utils.TokenManager
import io.ktor.server.routing.*
import io.ktor.server.routing.Route
import org.ktorm.database.Database

fun Route.authenticationRouter(db: Database, tokenManager: TokenManager)
{
        route("auth/"){
            signinAuth(db,tokenManager)
            signUpAuth(db,tokenManager)
        }

}
