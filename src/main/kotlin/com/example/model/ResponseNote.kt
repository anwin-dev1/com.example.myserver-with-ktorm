package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseNote(val note_id: Int, val notes: String)
