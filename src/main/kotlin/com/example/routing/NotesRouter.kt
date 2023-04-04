package com.example.routing


import com.example.model.InsertNoteMode
import com.example.model.ResponseData
import com.example.model.ResponseNote
import com.example.table.NoteTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Route.notesRouter(db: Database) {

    route("note") {
        get("/{user_id}/{note_id}") {
            var user_id = call.parameters["user_id"]?.toInt() ?: -1

            call.respondText { user_id.toString() }

            var note_id = call.request.queryParameters["note_id"]?.toInt() ?: -1

            val responseNote = db.from(NoteTable)
                .select()
                .where(NoteTable.user_id eq user_id)
                .map {
                    val note_id = it[NoteTable.note_id]!!
                    val notes = it[NoteTable.notes]!!
                    val updated_on = it[NoteTable.updated_on]!!
                    ResponseNote(note_id,notes,updated_on)
                }.firstOrNull()

            if(responseNote != null){
                call.respond(HttpStatusCode.OK,ResponseData(true,message = "success", data = responseNote))
            }
            else{
                call.respond(HttpStatusCode.OK,ResponseData(false,message = "No Data Found", data = "responseNote"))

            }

        }
        post("/{user_id}"){
            try {
                val user_id = call.request.queryParameters["user_id"]?.toInt()?:-1
                val data = call.receiveNullable<InsertNoteMode>()!!
                if(user_id == -1){
                    call.respond(HttpStatusCode.OK,ResponseData(false,"Params is Empty",data = "Error"))
                }else{
                    val value = db.insert(NoteTable){
                        set(it.notes,data.note)
                        set(it.user_id,user_id)
                    }
                    if(value == 1){
                        call.respond(HttpStatusCode.OK,ResponseData(true,"Data Inserted",data = data))
                    }else {
                        call.respond(HttpStatusCode.OK, ResponseData(false, "Some Issue", data = data))
                    }

                }
            }catch (e:Exception){
                call.respondText { e.message.toString() }
            }


//            if(value == 1){
//                call.respond(HttpStatusCode.OK,ResponseData(true,"Data Inserted",data = "Inserted"))
//            }
        }
    }



//    get("/getNote") {
//        val id = call.request.queryParameters["id"]?.toInt() ?: -1
//        val note = db.from(NoteTable).select().where { NoteTable.id eq id }.map {
//            val id = it[NoteTable.id]!!
//            val note = it[NoteTable.note]!!
//            NoteModel(note)
//        }.firstOrNull()
//        if (note != null) {
//            call.respond(HttpStatusCode.OK, ResponseData(true, data = note, message = "Successful"))
//        } else {
//            call.respond(HttpStatusCode.OK, ResponseData(false, data = "", message = "No Data Found"))
//        }
//    }
//
//    get("/getAllNotes") {
//        val notes = db.from(NoteTable).select()
//        val data = notes.map {
//            val id = it[NoteTable.id]
//            val note = it[NoteTable.note]
//            NotesDetailsModel(id, note)
//        }
//        val responseData = ResponseData(true, data = data, message = "Success Full")
//        call.respond(responseData)
//        return@get
//    }
//
//    post("/postNote") {
//
//        val testData: NoteModel
//        try {
//            testData = call.receiveNullable<NoteModel>()!!
//            val result = db.insert(NoteTable) {
//                set(it.note, testData.note)
//            }
//            if (result == 1) {
//                call.respond(HttpStatusCode.OK, ResponseData(success = true, data = testData, message = "Done"))
//            } else {
//                call.respond(
//                    HttpStatusCode.OK,
//                    ResponseData(success = false, data = "No ", message = "Some Error in Database")
//                )
//            }
//            return@post
//        } catch (exception: BadRequestException) {
//
//            call.respond(
//                HttpStatusCode.OK,
//                ResponseData(success = false, data = exception.message, message = "Body Format Wrong")
//            )
//            return@post
//        } catch (exception: ContentTransformationException) {
//            call.respond(
//                HttpStatusCode.OK, ResponseData(success = false, data = exception.message, message = "Empty Body")
//            )
//            return@post
//        }
//    }
//
//    delete("/deleteNoteById") {
//        val id = call.request.queryParameters["id"]?.toInt() ?: -1
//
//        val deleteNote = db.delete(NoteTable) { it.id eq id }
//
//        if (deleteNote == 1) {
//            call.respond(HttpStatusCode.OK, ResponseData(true, message = "Note Deleted in Id : $id", data = ""))
//        } else {
//            call.respond(HttpStatusCode.OK, ResponseData(false, message = "Id not found $id", data = ""))
//        }
//    }
//
//    put("/updateNote") {
//        val id = call.request.queryParameters["id"]?.toInt() ?: -1
//        try {
//            val note = call.receive<NoteModel>()
//            val updateNote = db.update(NoteTable) {
//                set(it.note, note.note)
//                where { it.id eq id }
//            }
//            if (updateNote == 1) {
//                call.respond(HttpStatusCode.OK, ResponseData(true, message = "Updated in $id", data = note))
//            } else {
//                call.respond(HttpStatusCode.OK, ResponseData(true, message = "Id Not Found in $id", data = ""))
//            }
//        } catch (e: Exception) {
//            call.respond(HttpStatusCode.OK, ResponseData(false, message = e.message, data = "Exception"))
//        }
//    }
}
