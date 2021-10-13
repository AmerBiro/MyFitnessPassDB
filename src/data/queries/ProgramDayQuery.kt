package com.androiddevs.data.queries

import com.noteapp.database.collections.ProgramDay
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programDay = database.getCollection<ProgramDay>()

suspend fun getProgramDay(owner: String): List<ProgramDay> {
    return programDay.find(ProgramDay::owner eq owner).toList()
}

suspend fun createProgramDay(programDay_: ProgramDay): Boolean {
    return programDay.insertOne(programDay_).wasAcknowledged()
}

suspend fun updateProgramDay(programDay_: ProgramDay): Boolean {
    val programDayExists = programDay.findOneById(programDay_.id) != null
    if (programDayExists) {
        return programDay.updateOneById(programDay_.id, programDay_).wasAcknowledged()
    }
    return false
}


suspend fun deleteProgramDay(programDayId: String): Boolean {
    val programDay_ = programDay.findOne(ProgramDay::id eq programDayId)
    programDay_?.let { programDay_ ->
        return programDay.deleteOneById(programDay_.id).wasAcknowledged()
    } ?: return false
}
