package com.example.routing.Route

import com.example.model.ResponseData
import com.example.model.UserAccount
import com.example.table.Users
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.io.File
import java.util.*

fun Route.userAccount(db: Database) {
    route("userAccount") {

        post("uploadProfilePic"){
            try{
                val multipart = call.receiveMultipart()
                var part: PartData.FileItem? = null
                multipart.forEachPart { currentPart ->
                    when (currentPart) {
                        is PartData.FileItem -> {
                            // If the part is a file, store it in the 'part' variable
                            part = currentPart
                        }

                        else -> {}
                    }
                    // If we have found a file, exit the loop
                    if (part != null) {
                        return@forEachPart
                    }
                    // If the current part is not a file, discard it
                    currentPart.dispose()
                }
                if (part == null) {
                    // If no file was found in the request, return an error response
                    call.respond(HttpStatusCode.OK, ResponseData(false, message = "File not found in request", data = ""))

                } else {
                    // Generate a unique file name
                    val timestamp = System.currentTimeMillis()
                    val randomString = UUID.randomUUID().toString()
                    val fileName = "$timestamp-$randomString.jpg"
                    // Do something with the file part
                    // For example, save it to disk
                    val file = File("/home/friday02/Ktor/uploadedImages/$fileName")
                    part?.streamProvider?.let { it1 -> it1() }?.use { input ->
                        file.outputStream().buffered().use {
                            input.copyTo(it)
                        }
                    }
                    // Return a success response
                    call.respond(HttpStatusCode.OK, ResponseData(true, message = "File uploaded successfully", data = ""))
                }
            }catch (e:Exception){
                call.respond(HttpStatusCode.OK, ResponseData(false, message = "Internal server error", data = e.localizedMessage))
                return@post
            }
        }

        get {
            try {
                val userId = call.request.queryParameters["user_id"]?.toInt() ?: -1
                if (userId != -1) {
                    val returnResponse = db.from(Users)
                        .select()
                        .where((Users.user_id eq userId)).map {
                            val user_id = it[Users.user_id]
                            val full_name = it[Users.full_name]
                            val first_name = it[Users.first_name]
                            val last_name = it[Users.last_name]
                            val mobile_number = it[Users.mobile_number]
                            val email_id = it[Users.email_id]
                            val dob = it[Users.dob]!!
                            val gender = it[Users.gender]
                            UserAccount(user_id = user_id,full_name= full_name,dob = dob,first_name = first_name,last_name = last_name,mobile_number = mobile_number,email_id = email_id, gender = gender)
                        }.firstOrNull()

                    if (returnResponse != null) {
                        call.respond(HttpStatusCode.OK, ResponseData(true, message = "success", data = returnResponse))
                        return@get
                    } else {
                        call.respond(HttpStatusCode.NotFound, ResponseData(false, message = "User not found", data = null))
                        return@get
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseData(false, message = "Invalid user ID", data = null))
                    return@get
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ResponseData(false, message = "Internal server error", data = e.localizedMessage))
                return@get
            }
        }

//        get {
//            try {
//                val userId = call.request.queryParameters["user_id"]?.toInt() ?: -1
//                if (userId != -1) {
//                    val returnResponse = db.from(Users)
//                        .select()
//                        .where((Users.user_id eq userId)).map {
//                            val user_id = it[Users.user_id]!!
//                            val full_name = it[Users.full_name]!!
//                            val first_name = it[Users.first_name]!!
//                            val last_name = it[Users.last_name]!!
//                            val mobile_number = it[Users.mobile_number]!!
//                            val email_id = it[Users.email_id]!!
//                            val dob = it[Users.dob]!!
//                            UserAccount(user_id,full_name,dob,first_name,last_name,mobile_number,email_id)
//                        }.firstOrNull()
//
//                    if (returnResponse != null) {
//                        call.respond(HttpStatusCode.OK, ResponseData(true, message = "success", data = returnResponse))
//                        return@get
//                    } else {
//                        call.respond(
//                            HttpStatusCode.OK,
//                            ResponseData(false, message = "No Data Found", data = "Response Note")
//                        )
//                        return@get
//                    }
//
//                } else {
//                    call.respond(HttpStatusCode.OK, ResponseData(false, "Some Issue In UserId", ""))
//                    return@get
//                }
//            } catch (e: Exception) {
//                call.respond(HttpStatusCode.OK, ResponseData(false, e.message, "Exception"))
//                return@get
//            }
//        }

        put {
            try {
                val userId = call.request.queryParameters["user_id"]?.toInt() ?: -1
                val updatingDetails = call.receiveNullable<JsonObject>()

                val full_name = updatingDetails?.get("full_name")?.jsonPrimitive?.contentOrNull
                val firstName = updatingDetails?.get("first_name")?.jsonPrimitive?.contentOrNull
                val lastName = updatingDetails?.get("last_name")?.jsonPrimitive?.contentOrNull
                val mobileNumber = updatingDetails?.get("mobile_number")?.jsonPrimitive?.contentOrNull
                val emailId = updatingDetails?.get("email_id")?.jsonPrimitive?.contentOrNull
                val gender = updatingDetails?.get("gender")?.jsonPrimitive?.contentOrNull
                val dob = updatingDetails?.get("dob")?.jsonPrimitive?.contentOrNull

                if (userId != -1) {
                    if (updatingDetails != null) {
                        when {
                            updatingDetails.containsKey("full_name") -> {
                                val action = db.update(Users) {
                                    set(it.full_name, full_name)
                                    where {
                                        (it.user_id eq userId)
                                    }
                                }
                                actionFun(call, action)
                                return@put
                            }

                            updatingDetails.containsKey("first_name") -> {
                                val action = db.update(Users) {
                                    set(it.first_name, firstName)
                                    where {
                                        (it.user_id eq userId)
                                    }
                                }
                                actionFun(call, action)
                                return@put
                            }

                            updatingDetails.containsKey("last_name") -> {
                                val action = db.update(Users) {
                                    set(it.last_name, lastName)
                                    where {
                                        (it.user_id eq userId)
                                    }
                                }
                                actionFun(call, action)
                                return@put
                            }

                            updatingDetails.containsKey("mobile_number") -> {
                                val action = db.update(Users) {
                                    set(it.mobile_number, mobileNumber)
                                    where {
                                        (it.user_id eq userId)
                                    }
                                }
                                actionFun(call, action)
                                return@put
                            }

                            updatingDetails.containsKey("email_id") -> {
                                val action = db.update(Users) {
                                    set(it.email_id, emailId)
                                    where {
                                        (it.user_id eq userId)
                                    }
                                }
                                actionFun(call, action)
                                return@put
                            }

                            updatingDetails.containsKey("dob") -> {
                                val action = db.update(Users) {
                                    set(it.dob, dob)
                                    where {
                                        (it.user_id eq userId)
                                    }
                                }
                                actionFun(call, action)
                                return@put
                            }

                            updatingDetails.containsKey("gender") -> {
                                val action = db.update(Users) {
                                    set(it.gender, gender)
                                    where {
                                        (it.user_id eq userId)
                                    }
                                }
                                actionFun(call, action)
                                return@put
                            }

                            else -> {
                                call.respond(HttpStatusCode.OK, ResponseData(false, "No Key Present", ""))
                                return@put
                            }
                        }
                    }
                } else {
                    call.respond(HttpStatusCode.OK, ResponseData(false, "Data Type Issue", ""))
                    return@put
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.OK, ResponseData(false, e.message, ""))
                return@put
            }
        }
    }

}

suspend fun actionFun(call: ApplicationCall, action: Int) {
    if (action == 1) {
        // Respond with a success message
        call.respond(HttpStatusCode.OK, ResponseData(true, "Data Updated", ""))
    } else {
        // Respond with an error message
        call.respond(HttpStatusCode.OK, ResponseData(false, "Database Updating Issue", ""))
    }
}

