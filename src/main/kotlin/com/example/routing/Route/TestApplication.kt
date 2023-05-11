package com.example.routing.Route

import com.example.model.ResponseData
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun Route.testApi() {

    route("testJsonObject") {
        get {
            try {
                val jsonData = call.receiveNullable<JsonObject>()
//!!!!!_______________________________________________________________________________________
                val map = mapOf(
                    "name" to JsonPrimitive("John"),
                    "age" to JsonPrimitive(30),
                    "address" to JsonObject(
                        mapOf(
                            "street" to JsonPrimitive("123 Main St"),
                            "city" to JsonPrimitive("Anytown"),
                            "state" to JsonPrimitive("CA"),
                            "zip" to JsonPrimitive("12345")
                        )
                    )
                )
                val body1 = JsonObject(map)
//!!!!!_______________________________________________________________________________________
                val body2 = JsonObject(
                    mapOf(
                        "age" to JsonPrimitive(2),
                        "login" to JsonPrimitive("email"),
                    )
                )
//!!!!!_______________________________________________________________________________________
                val body3 = JsonObject(
                    mapOf(
                        "name" to JsonPrimitive("John"),
                        "age" to JsonPrimitive(30),
                        "address" to JsonObject(
                            mapOf(
                                "street" to JsonPrimitive("123 Main St"),
                                "city" to JsonPrimitive("Anytown"),
                                "state" to JsonPrimitive("CA"),
                                "zip" to JsonPrimitive("12345")
                            )
                        )
                    )
                )

                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(
                        true,
                        message = "Success",
                        data = body3
                    )
                )

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(
                        false,
                        message = e.localizedMessage,
                        data = "Exception"
                    )
                )
            }


        }
    }
}

