package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.LoginDetailsModel
import com.example.model.UserAccount
import com.example.table.Users
import io.ktor.server.config.*
import java.util.*

class TokenManager(val config: HoconApplicationConfig) {

    fun generateJWTToken(user: LoginDetailsModel): String {
        val audiance = config.property("audiance").getString()
        val secret = config.property("secret").getString()
        val issuer = config.property("issuer").getString()
        val exirationTime = System.currentTimeMillis() + 60000
        val nonce = UUID.randomUUID().toString()


        val token = JWT.create()
            .withAudience(audiance)
            .withIssuer(issuer)
            .withClaim("email",user.email)
            .withClaim("id",user.id)
            .withClaim("nonce", nonce)
            .sign(Algorithm.HMAC256(secret))

        return token
    }
}