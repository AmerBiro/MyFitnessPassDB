package com.androiddevs.data.queries

import com.androiddevs.data.collections.ProgramNote
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programsNotes = database.getCollection<ProgramNote>()

suspend fun createProgramNotes(programsNote: ProgramNote): Boolean {
    return programsNotes.insertOne(programsNote).wasAcknowledged()
}

suspend fun updateProgramNotes(programsNote: ProgramNote): Boolean {
    val programNoteExists = programsNotes.findOneById(programsNote.id) != null
    if (programNoteExists) {
        return programsNotes.updateOneById(programsNote.id, programsNote).wasAcknowledged()
    }
    return false
}

suspend fun getProgramsNotes(parent: String): List<ProgramNote> {
    return programsNotes.find(ProgramNote::parent eq parent).toList()
}

suspend fun deleteProgramNote(programsNoteId: String): Boolean {
    val programsNote = programsNotes.findOne(ProgramNote::id eq programsNoteId)
    programsNote?.let { programsNote ->
        return programsNotes.deleteOneById(programsNote.id).wasAcknowledged()
    } ?: return false
}
