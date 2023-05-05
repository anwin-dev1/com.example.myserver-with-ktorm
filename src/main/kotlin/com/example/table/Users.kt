package com.example.table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Users : Table<Nothing>("users") {
    val user_id = int("user_id").primaryKey()
    val full_name = varchar("full_name")
    val first_name = varchar("first_name")
    val last_name = varchar("last_name")
    val user_name = varchar("user_name")
    val dob = varchar("dob")
    val mobile_number = varchar("mobile_number")
    val email_id = varchar("email_id")
    val gender = varchar("gender")
    val password = varchar("password")// @Todo Need to add Column in DB table
}