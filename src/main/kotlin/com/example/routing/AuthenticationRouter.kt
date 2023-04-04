package com.example.routing


import com.example.route.signUpAuth
import com.example.route.signinAuth
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Route.authenticationRouter(db:Database)
{
        route("auth/"){
            signinAuth(db)
            signUpAuth(db)
        }

}