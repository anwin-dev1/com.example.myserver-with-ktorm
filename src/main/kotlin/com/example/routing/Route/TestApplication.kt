package com.example.routing.Route

import com.example.model.ResponseData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import kotlinx.serialization.json.JsonObject

import io.ktor.server.routing.*
fun Route.testApi() {

    route("testJsonObject") {
        get {
            val jsonData = call.receiveNullable<JsonObject>()

            if (jsonData != null) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(
                        true,
                        message = "Success",
                        data = "Key is ${jsonData["name"]} : Value is ${jsonData.values}"
                    )
                )
            }
        }
    }
}
