package com.androiddevs.data.queries

import com.noteapp.database.collections.Coach
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val coach = database.getCollection<Coach>()

suspend fun getCoach(userId: String): List<Coach> {
    return coach.find(Coach::ownersId contains userId).toList()
}

suspend fun createUpdateCoach(coach_: Coach): Boolean{
    val coachExists = coach.findOneById(coach_.id) != null
    return if (coachExists){
        coach.updateOneById(coach_.id, coach_).wasAcknowledged()
    }else{
        coach.insertOne(coach_).wasAcknowledged()
    }
}