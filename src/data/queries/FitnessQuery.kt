package com.androiddevs.data.queries

import com.noteapp.database.collections.Fitness
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val fitness = database.getCollection<Fitness>()

suspend fun getFitness(parent: String): List<Fitness> {
    return fitness.find(Fitness::parent eq parent).toList()
}

suspend fun createFitness(fitness_: Fitness): Boolean {
    return fitness.insertOne(fitness_).wasAcknowledged()
}

suspend fun updateFitness(fitness_: Fitness): Boolean {
    val fitnessExists = fitness.findOneById(fitness_.id) != null
    if (fitnessExists) {
        return fitness.updateOneById(fitness_.id, fitness_).wasAcknowledged()
    }
    return false
}


suspend fun deleteFitness(fitnessId: String): Boolean {
    val fitness_ = fitness.findOne(Fitness::id eq fitnessId)
    fitness_?.let { fitness_ ->
        return fitness.deleteOneById(fitness_.id).wasAcknowledged()
    } ?: return false
}
