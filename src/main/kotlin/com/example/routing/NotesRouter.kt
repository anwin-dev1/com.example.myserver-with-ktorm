package com.example.routing


import com.example.model.InsertNoteMode
import com.example.model.ResponseData
import com.example.model.ResponseNote
import com.example.table.NoteTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Route.notesRouter(db: Database) {

    route("note") {
        get("getNotes") {
            try {
                val noteId = call.request.queryParameters["note_id"]?.toInt() ?: -1
                val userId = call.request.queryParameters["user_id"]?.toInt() ?: -1

                val responseNote = db.from(NoteTable)
                    .select()
                    .where((NoteTable.user_id eq userId) and (NoteTable.note_id eq noteId))
                    .map {
                        val noteId = it[NoteTable.note_id]!!
                        val notes = it[NoteTable.notes]!!
                        ResponseNote(noteId, notes)
                    }.firstOrNull()

                if (responseNote != null) {
                    call.respond(HttpStatusCode.OK, ResponseData(true, message = "success", data = responseNote))
                    return@get
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseData(false, message = "No Data Found", data = "Response Note")
                    )
                    return@get
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(false, message = e.localizedMessage.toString(), data = "Exception")
                )
                return@get
            }
        }
        post("addNotes") {
            try {
                val user_id = call.request.queryParameters["user_id"]?.toInt() ?: -1
                val data = call.receiveNullable<InsertNoteMode>()!!
                if (user_id == -1) {
                    call.respond(HttpStatusCode.OK, ResponseData(false, "Params is Empty", data = "Error"))
                    return@post
                } else {
                    val value = db.insert(NoteTable) {
                        set(it.notes, data.note)
                        set(it.user_id, user_id)
                    }
                    if (value == 1) {
                        call.respond(HttpStatusCode.OK, ResponseData(true, "Data Inserted", data = data))
                        return@post
                    } else {
                        call.respond(HttpStatusCode.OK, ResponseData(false, "Some Issue", data = data))
                        return@post
                    }

                }
            } catch (e: Exception) {
                call.respondText { e.message.toString() }
            }
        }
        delete("deleteNotes") {
            try {
                val noteId = call.request.queryParameters["note_id"]?.toInt() ?: -1
                val userId = call.request.queryParameters["user_id"]?.toInt() ?: -1

                if (noteId != -1 && userId != -1) {
                    val action = db.delete(NoteTable) {
                        (it.user_id eq userId) and (it.note_id eq noteId)
                    }
                    if (action == 1) {
                        call.respond(HttpStatusCode.OK, ResponseData(true, "Data Deleted", ""))
                        return@delete
                    } else {
                        call.respond(HttpStatusCode.OK, ResponseData(false, "Some Issue While Deleting", ""))
                        return@delete
                    }
                } else {
                    call.respond(HttpStatusCode.OK, ResponseData(false, "else", ""))
                    return@delete
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.OK, ResponseData(false, e.message, ""))
                return@delete
            }
        }

        put("updateNotes") {
            try {
                val noteId = call.request.queryParameters["note_id"]?.toInt() ?: -1
                val userId = call.request.queryParameters["user_id"]?.toInt() ?: -1
                val noteData = call.receiveNullable<InsertNoteMode>()
                if (noteId != -1 && userId != -1) {

                    val action = db.update(NoteTable) {
                        set(it.notes, noteData?.note)
                        where {
                            (it.user_id eq userId) and (it.note_id eq noteId)
                        }
                    }

                    if (action == 1) {
                        call.respond(HttpStatusCode.OK, ResponseData(true, "Data Updated", ""))
                        return@put
                    } else {
                        call.respond(HttpStatusCode.OK, ResponseData(false, "Some Issue While Updating", ""))
                        return@put
                    }
                } else {
                    call.respond(HttpStatusCode.OK, ResponseData(false, "else", ""))
                    return@put
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.OK, ResponseData(false, e.message, ""))
                return@put
            }
        }

        get("getAllNotes") {
            try {
                val userId = call.request.queryParameters["user_id"]?.toInt() ?: -1
                val notesList = ArrayList<String>()/**/

                val notes = db.from(NoteTable)
                    .select()
                    .orderBy(NoteTable.note_id.desc())
                    .where { NoteTable.user_id eq userId }

                if (notes == null) {
                    call.respond(HttpStatusCode.NotFound, ResponseData(false, "No notes found", null))
                    return@get
                } else {
                    notes.forEach {
                        val noteDetails = it[NoteTable.notes]
//                        notesList.add(InsertNoteMode(noteDetails!!))
                        notesList.add(noteDetails.toString())
                    }
                    call.respond(HttpStatusCode.OK, ResponseData(true, "Success",notesList))
                    return@get
                }

            } catch (e: Exception) {
                call.respond(HttpStatusCode.OK, ResponseData(false, e.message, ""))
                return@get
            }
        }
    }
}

