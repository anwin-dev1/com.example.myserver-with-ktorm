package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    val user_id: Int,
    val full_name: String,
    val dob: String,
    val first_name: String,
    val last_name: String,
    val mobile_number: String,
    val email_id: String
)