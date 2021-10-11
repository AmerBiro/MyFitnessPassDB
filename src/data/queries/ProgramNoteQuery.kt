package com.androiddevs.data.queries

import com.androiddevs.data.collections.ProgramNote
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programNotes = database.getCollection<ProgramNote>()

suspend fun getProgramNotes(programId: String): List<ProgramNote> {
    return programNotes.find(ProgramNote::ownersId contains programId).toList()
}

suspend fun createUpdateProgramNotes(programNotes_: ProgramNote): Boolean{
    val programNotesExists = programNotes.findOneById(programNotes_.id) != null
    return if (programNotesExists){
        programNotes.updateOneById(programNotes_.id, programNotes_).wasAcknowledged()
    }else{
        programNotes.insertOne(programNotes_).wasAcknowledged()
    }
}