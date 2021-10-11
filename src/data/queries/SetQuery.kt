package com.androiddevs.data.queries

import com.noteapp.database.collections.Program
import com.noteapp.database.collections.Sets
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val sets = database.getCollection<Sets>()

suspend fun getSets(exerciseId: String): List<Sets> {
    return sets.find(Sets::ownersId contains exerciseId).toList()
}

suspend fun createUpdateSets(sets_: Sets): Boolean{
    val setsExists = sets.findOneById(sets_.id) != null
    return if (setsExists){
        sets.updateOneById(sets_.id, sets_).wasAcknowledged()
    }else{
        sets.insertOne(sets_).wasAcknowledged()
    }
}