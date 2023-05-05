package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class InsertNoteMode(var title:String,val note:String)
