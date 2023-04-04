package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    val success: Boolean? = null,
    val message: String? = null,
    val data: T? = null
)