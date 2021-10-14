package com.androiddevs.data.queries

import com.noteapp.database.collections.ProgramDay
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programDays = database.getCollection<ProgramDay>()

suspend fun getProgramDay(parent: String): List<ProgramDay> {
    return programDays.find(ProgramDay::parent eq parent).toList()
}

suspend fun createProgramDay(programDay: ProgramDay): Boolean {
    return programDays.insertOne(programDay).wasAcknowledged()
}

suspend fun updateProgramDay(programDay: ProgramDay): Boolean {
    val programDayExists = programDays.findOneById(programDay.id) != null
    if (programDayExists) {
        return programDays.updateOneById(programDay.id, programDay).wasAcknowledged()
    }
    return false
}


suspend fun deleteProgramDay(programDayId: String): Boolean {
    val programDay = programDays.findOne(ProgramDay::id eq programDayId)
    programDay?.let { programDay ->
        return programDays.deleteOneById(programDay.id).wasAcknowledged()
    } ?: return false
}
