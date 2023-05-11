package com.example.table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object LoginOrRegister:Table<Nothing>("userLogin") {
    val id = int("id").primaryKey()
    val email = varchar("email")
    val password = varchar("password")
}