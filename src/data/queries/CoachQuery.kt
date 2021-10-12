package com.androiddevs.data.queries

import com.noteapp.database.collections.Coach
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val coach = database.getCollection<Coach>()

suspend fun createCoach(coach_: Coach): Boolean {
    return coach.insertOne(coach_).wasAcknowledged()
}

suspend fun updateCoach(coach_: Coach): Boolean {
    val coachExists = coach.findOneById(coach_.id) != null
    if (coachExists) {
        return coach.updateOneById(coach_.id, coach_).wasAcknowledged()
    }
    return false
}

suspend fun shareCoachWithOthers(coachId: String, email: String): Boolean {
    // :TODO check if the entered email does not exists in the list
    val coach_ = coach.findOne(Coach::id eq coachId)
    coach_?.let { coach_ ->
        val newHasAccess = coach_.hasAccess + email
        val updateResult = coach.updateOne(Coach::id eq coach_.id, setValue(Coach::hasAccess, newHasAccess))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun getOwnCoach(owner: String): List<Coach> {
    return coach.find(Coach::owner eq owner).toList()
}

suspend fun getCoachSharedWIthMe(email: String): List<Coach> {
    return coach.find(Coach::hasAccess contains email).toList()
}


suspend fun deleteCoach(owner: String, coachId: String): Boolean {
    val coach_ = coach.findOne(Coach::owner eq owner, Coach::id eq coachId)
    coach_?.let { coach_ ->
        return coach.deleteOneById(coach_.id).wasAcknowledged()
    } ?: return false
}

suspend fun removeCoachFromSharedWithMeList(coachId: String, email: String): Boolean {
    val coach_ = coach.findOne(Coach::id eq coachId, Coach::hasAccess contains email)
    coach_?.let { coach_ ->
        val newHasAccess = coach_.hasAccess - email
        val updateResult = coach.updateOne(Coach::id eq coach_.id, setValue(Program::hasAccess, newHasAccess))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun getFavoriteCoach(owner: String): List<Coach> {
    return coach.find(Coach::owner eq owner, Coach::favoriteStatus eq 1).toList()
}

suspend fun addCoachToFavorite(coachId: String): Boolean {
    val coach_ = coach.findOne(Coach::id eq coachId, Coach::favoriteStatus ne 1)
    coach_?.let { coach_ ->
        val newFavoriteStatus = 1
        val updateResult = coach.updateOne(Coach::id eq coach_.id, setValue(Coach::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun removeCoachFromFavorite(coachId: String): Boolean {
    val coach_ = coach.findOne(Coach::id eq coachId, Coach::favoriteStatus ne 0)
    coach_?.let { coach_ ->
        val newFavoriteStatus = 0
        val updateResult = coach.updateOne(Coach::id eq coach_.id, setValue(Coach::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}