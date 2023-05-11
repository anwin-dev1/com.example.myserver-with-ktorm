package com.example.route

import com.example.model.LoginDetailsModel
import com.example.model.ResponseData
import com.example.table.Users
import com.example.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.JsonObject
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Route.signUpAuth(db: Database, tokenManager: TokenManager) {
    post("signup") {
        try {
            val loginDataReceived = call.receive<LoginDetailsModel>()

            val email = loginDataReceived.email.lowercase()
            val password = loginDataReceived.hashPassword()

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

            //check if email already exists
            val userEmailExist = db.from(Users)
                .select()
                .where { Users.email_id eq email }
                .map { it[Users.email_id]  }
                .firstOrNull()

            if (userEmailExist != null) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(
                        false,
                        "User already exists with this name, Please try with another userName",
                        data = loginDataReceived
                    )
                )
                return@post
            }else{
                val result = db.insert(Users) {
                    set(it.email_id, email)
                    set(it.password, password)
                }
                if (result == 1) {
                    val response = JsonObject(
                        mapOf(
                            "user_id" to JsonPrimitive(0),
                            "email_id" to JsonPrimitive(""),
                            "token" to JsonPrimitive(""),
                        )
                    )
                    call.respond(HttpStatusCode.OK, ResponseData(true, "Inserted", data = response))
                } else {
                    call.respond(HttpStatusCode.OK, ResponseData(true, "Some Issue in DB", data = "Error"))
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.OK, ResponseData(false, e.message, data = "Exception"))
        }
    }
}
fun Route.  signinAuth(db: Database, tokenManager: TokenManager) {

    post("signinwithmobile"){

    }


    post("signin") {

        try {
            val loginDataReceived = call.receive<LoginDetailsModel>()

            val email = loginDataReceived.email.lowercase()
            val password = loginDataReceived.password//.hashPassword()

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
                    val id = it[Users.user_id]
                    val emailID = it[Users.email_id]!!
                    val password = it[Users.password]!!
                    LoginDetailsModel(id,emailID, password)
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
                    val token = tokenManager.generateJWTToken(user)
                    val user_id = user.id ?: -1
                    val update_r_insert_token = db.update(Users)
                    {
                        set(Users.token,token)
                        where {
                            it.user_id eq user_id
                        }
                    }

                    if(update_r_insert_token == 1){
                        val response = JsonObject(
                            mapOf(
                                "user_id" to JsonPrimitive(user.id),
                                "email_id" to JsonPrimitive(user.email),
                                "token" to JsonPrimitive(token),
                            )
                        )

                        call.respond(
                            HttpStatusCode.OK,
                            ResponseData(true, message = "Validation Success", data = response)
                        )
                    }else{
                        call.respond(
                            HttpStatusCode.OK,
                            ResponseData(false, message = "Token Not Inserted : $token", data = token)
                        )
                    }
                    return@post
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.OK, ResponseData(false, e.message, data = "Exception"))
            return@post
        }
    }
}