package com.androiddevs.data.queries

import com.androiddevs.data.collections.Note
import com.androiddevs.data.collections.User
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programs = database.getCollection<Program>()

suspend fun getPrograms(userId: String): List<Program> {
    return programs.find(Program::ownersId contains userId).toList()
}

suspend fun createUpdateProgram(program: Program): Boolean{
    val programExists = programs.findOneById(program.id) != null
    return if (programExists){
        programs.updateOneById(program.id, program).wasAcknowledged()
    }else{
        programs.insertOne(program).wasAcknowledged()
    }
}

