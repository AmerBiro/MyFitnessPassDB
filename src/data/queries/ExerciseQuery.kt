package com.androiddevs.data.queries

import com.noteapp.database.collections.Exercise
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val exercises = database.getCollection<Exercise>()

suspend fun getExercises(dayId: String): List<Exercise> {
    return exercises.find(Exercise::ownersId contains dayId).toList()
}

suspend fun createUpdateExercises(exercise: Exercise): Boolean{
    val exercisesExists = exercises.findOneById(exercise.id) != null
    return if (exercisesExists){
        exercises.updateOneById(exercise.id, exercise).wasAcknowledged()
    }else{
        exercises.insertOne(exercise).wasAcknowledged()
    }
}