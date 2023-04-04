package com.example.table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Users : Table<Nothing>("users") {
    val userId = int("userId").primaryKey()
    val first_name = varchar("first_name")
    val last_name = varchar("last_name")
    val user_name = varchar("user_name")
    val mobile_number = varchar("mobile_number")
    val email_id = varchar("email_id")
    val password = varchar("password")// @Todo Need to add Column in DB table
}