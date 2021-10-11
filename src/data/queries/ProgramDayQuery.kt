package com.androiddevs.data.queries

import com.noteapp.database.collections.Program
import com.noteapp.database.collections.ProgramDay
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programDay = database.getCollection<ProgramDay>()

suspend fun getProgramDay(programId: String): List<ProgramDay> {
    return programDay.find(ProgramDay::ownersId contains programId).toList()
}

suspend fun createUpdateProgramDay(programDay_: ProgramDay): Boolean{
    val programDayExists = programDay.findOneById(programDay_.id) != null
    return if (programDayExists){
        programDay.updateOneById(programDay_.id, programDay_).wasAcknowledged()
    }else{
        programDay.insertOne(programDay_).wasAcknowledged()
    }
}