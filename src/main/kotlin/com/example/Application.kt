package com.example

import com.example.db.DatabaseFactory
import com.example.routing.authenticationRouter
import com.example.routing.mainRouter
import com.example.routing.notesRouter
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    val db = DatabaseFactory.init()
    mainRouter(db)
//    notesRouter(db)

}
//Ktorm GitHub Documentation @Todo https://github.com/kotlin-orm/ktorm

//    val scheduler = Scheduler()
//    scheduler.scheduleJob(
//        jobName = "myJob",
//        jobGroup = "myGroup",
//        triggerName = "myTrigger",
//        triggerGroup = "myGroup",
//        cronExpression = "0 0 12 * * ?",
//        jobData = JobDataMap(mapOf("param1" to "value1")),
//        jobClass = TestScheduler::class.java
//    )
//    scheduler.start()