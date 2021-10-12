package com.androiddevs.data.queries

import com.noteapp.database.collections.Fitness
import com.noteapp.database.collections.Info
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val fitness = database.getCollection<Fitness>()

suspend fun getFitness(email: String): List<Fitness> {
    return fitness.find(Fitness::hasAccess contains email).toList()
}

suspend fun createUpdateFitness(fitness_: Fitness): Boolean{
    val fitnessExists = fitness.findOneById(fitness_.id) != null
    return if (fitnessExists){
        fitness.updateOneById(fitness_.id, fitness_).wasAcknowledged()
    }else{
        fitness.insertOne(fitness_).wasAcknowledged()
    }
}