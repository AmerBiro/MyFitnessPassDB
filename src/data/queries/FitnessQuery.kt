package com.androiddevs.data.queries

import com.noteapp.database.collections.Fitness
import com.noteapp.database.collections.Program
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val fitness = database.getCollection<Fitness>()

suspend fun getFitness(owner: String): List<Fitness> {
    return fitness.find(Fitness::owner eq owner).toList()
}

suspend fun createFitness(fitness_: Fitness): Boolean {
    return fitness.insertOne(fitness_).wasAcknowledged()
}

//suspend fun updateFitness(program: Program): Boolean {
//    val programExists = programs.findOneById(program.id) != null
//    if (programExists) {
//        return programs.updateOneById(program.id, program).wasAcknowledged()
//    }
//    return false
//}
//
//
//suspend fun deleteFitness(owner: String, programId: String): Boolean {
//    val program = programs.findOne(Program::owner eq owner, Program::id eq programId)
//    program?.let { program ->
//        return programs.deleteOneById(program.id).wasAcknowledged()
//    } ?: return false
//}
