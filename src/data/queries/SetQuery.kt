package com.androiddevs.data.queries

import com.noteapp.database.collections.Fitness
import com.noteapp.database.collections.Program
import com.noteapp.database.collections.Sets
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val sets = database.getCollection<Sets>()

suspend fun getSets(parent: String): List<Sets> {
    return sets.find(Sets::parent eq parent).toList()
}

suspend fun createSets(sets_: Sets): Boolean {
    return sets.insertOne(sets_).wasAcknowledged()
}

suspend fun updateSets(sets_: Sets): Boolean {
    val setsExists = sets.findOneById(sets_.id) != null
    if (setsExists) {
        return sets.updateOneById(sets_.id, sets_).wasAcknowledged()
    }
    return false
}


suspend fun deleteSets(setsId: String): Boolean {
    val sets_ = sets.findOne(Sets::id eq setsId)
    sets_?.let { sets_ ->
        return sets.deleteOneById(sets_.id).wasAcknowledged()
    } ?: return false
}
