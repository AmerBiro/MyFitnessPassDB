package com.androiddevs.data.queries

import com.noteapp.database.collections.Exercise
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val exercises = database.getCollection<Exercise>()

suspend fun createExercises(exercise: Exercise): Boolean {
    return exercises.insertOne(exercise).wasAcknowledged()
}

suspend fun updateExercises(exercise: Exercise): Boolean {
    val exerciseExists = exercises.findOneById(exercise.id) != null
    if (exerciseExists) {
        return exercises.updateOneById(exercise.id, exercise).wasAcknowledged()
    }
    return false
}

suspend fun getExercises(parent: String): List<Exercise> {
    return exercises.find(Exercise::parent eq parent).toList()
}


suspend fun isExerciseOwner(exerciseId: String, owner: String): Boolean{
    val exercise = exercises.findOneById(exerciseId) ?: return false
    return exercise.owner == owner
}

suspend fun isExerciseAlreadyShared(exerciseId: String, email: String): Boolean{
    val exercise = exercises.findOneById(exerciseId)?: return true
    return email in exercise.hasAccess
}

suspend fun shareExerciseWithOthers(exerciseId: String, email: String): Boolean {
    val hasAccess = exercises.findOneById(exerciseId)?.hasAccess ?: return false
    return exercises.updateOneById(exerciseId, setValue(Exercise::hasAccess, hasAccess + email)).wasAcknowledged()
}



suspend fun getExercisesSharedWIthMe(email: String): List<Exercise> {
    return exercises.find(Exercise::hasAccess contains email).toList()
}


suspend fun deleteExercise(exerciseId: String): Boolean {
    val exercise = exercises.findOne(Exercise::id eq exerciseId)
    exercise?.let { exercise ->
        return exercises.deleteOneById(exercise.id).wasAcknowledged()
    } ?: return false
}

suspend fun removeExerciseFromSharedWithMeList(exerciseId: String, email: String): Boolean {
    val exercise = exercises.findOne(Exercise::id eq exerciseId, Exercise::hasAccess contains email)
    exercise?.let { exercise ->
        val newHasAccess = exercise.hasAccess - email
        val updateResult = exercises.updateOne(Exercise::id eq exercise.id, setValue(Exercise::hasAccess, newHasAccess))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun getFavoriteExercises(parent: String): List<Exercise> {
    return exercises.find(Exercise::parent eq parent, Exercise::favoriteStatus eq 1).toList()
}

suspend fun addExerciseToFavorite(exerciseId: String): Boolean {
    val exercise = exercises.findOne(Exercise::id eq exerciseId, Exercise::favoriteStatus ne 1)
    exercise?.let { exercise ->
        val newFavoriteStatus = 1
        val updateResult = exercises.updateOne(Exercise::id eq exercise.id, setValue(Exercise::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun removeExerciseFromFavorite(exerciseId: String): Boolean {
    val exercise = exercises.findOne(Exercise::id eq exerciseId, Exercise::favoriteStatus ne 0)
    exercise?.let { exercise ->
        val newFavoriteStatus = 0
        val updateResult = exercises.updateOne(Exercise::id eq exercise.id, setValue(Exercise::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}