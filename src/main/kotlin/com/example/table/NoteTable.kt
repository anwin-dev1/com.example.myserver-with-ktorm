package com.example.table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object NoteTable:Table<Nothing>("note") {
    val note_id = int("note_id").primaryKey()
    val user_id = int("user_id")
    val notes = varchar("notes")
    val created_on = varchar("created_on")
    val updated_on = varchar("update_on")
}