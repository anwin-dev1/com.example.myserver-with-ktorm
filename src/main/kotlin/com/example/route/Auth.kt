package com.example.route

import com.example.model.LoginDetailsModel
import com.example.model.ResponseData
import com.example.table.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Route.signUpAuth(db: Database) {
    post("signup") {
        try {
            val loginDataReceived = call.receive<LoginDetailsModel>()

            val userName = loginDataReceived.userName?.lowercase()
            val password = loginDataReceived.hashPassword()
            val email = loginDataReceived.email.lowercase()

            if (!loginDataReceived.isEmailPatternMatch()) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(false, "Invalid Email Formate", data = loginDataReceived)
                )
                return@post
            }

            if (!loginDataReceived.isValie()) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(false, "Password requires 8+ chars with 1 uppercase, 1 lowercase, 1 number, and \"@,#,_,!\"", data = loginDataReceived)
                )
                return@post
            }

            //check if username already exists
            val userNameExist = db.from(Users)
                .select()
                .where { Users.email_id eq email }
                .map { it[Users.email_id] }
                .firstOrNull()

            if (userNameExist != null) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(
                        false,
                        "User already exists with this name, Please try with another userName",
                        data = loginDataReceived
                    )
                )
                return@post
            }

            val result = db.insert(Users) {
                set(it.email_id, email)
                set(it.password, password)
            }
            if (result == 1) {
                call.respond(HttpStatusCode.OK, ResponseData(true, "Inserted", data = loginDataReceived))
            } else {
                call.respond(HttpStatusCode.OK, ResponseData(true, "Some Issue in DB", data = "Error"))
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.OK, ResponseData(false, e.message, data = "Exception"))
        }
    }
}

fun Route.signinAuth(db: Database) {

    post("signin") {

        try {
            val loginDataReceived = call.receive<LoginDetailsModel>()

            val userName = loginDataReceived.userName?.lowercase()
            val password = loginDataReceived.password//.hashPassword()
            val email = loginDataReceived.email.lowercase()

            if (!loginDataReceived.isEmailPatternMatch()) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(false, "Invalid Email Formate", data = loginDataReceived)
                )
                return@post
            }

            if (!loginDataReceived.isValie()) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(
                        false,
                        "Password requires 8+ chars with 1 uppercase, 1 lowercase, 1 number, and \"@,#,_,!\"",
                        data = loginDataReceived
                    )
                )
                return@post
            }

            val user = db.from(Users).select()
                .where { Users.email_id eq email }
                .map {
                    val id = it[Users.userId]!!
                    val emailID = it[Users.email_id]!!
                    val password = it[Users.password]!!
                    LoginDetailsModel(id, userName, emailID, password)
                }.firstOrNull()

            if (user == null) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(false, message = "Email Doesn't exists", data = "Exception")
                )
                return@post
            } else {
                val passwordCheck = BCrypt.checkpw(password, user.password)
                if (!passwordCheck) {
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseData(false, message = "Invalid Password", data = "Exception")
                    )
                    return@post
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseData(true, message = "Validation Success", data = "Exception")
                    )
                    return@post
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.OK, ResponseData(false, e.message, data = "Exception"))
            return@post
        }
    }
}