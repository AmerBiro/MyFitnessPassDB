package com.androiddevs.data.queries

import com.noteapp.database.collections.Coach
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val coaches = database.getCollection<Coach>()

suspend fun createCoach(coach: Coach): Boolean {
    return coaches.insertOne(coach).wasAcknowledged()
}

suspend fun updateCoach(coach: Coach): Boolean {
    val coachExists = coaches.findOneById(coach.id) != null
    if (coachExists) {
        return coaches.updateOneById(coach.id, coach).wasAcknowledged()
    }
    return false
}

suspend fun getCoaches(parent: String): List<Coach> {
    return coaches.find(Coach::parent eq parent).toList()
}

suspend fun isCoachOwner(coachId: String, owner: String): Boolean{
    val coach = coaches.findOneById(coachId) ?: return false
    return coach.owner == owner
}

suspend fun isCoachAlreadyShared(coachId: String, email: String): Boolean{
    val coach = coaches.findOneById(coachId)?: return true
    return email in coach.hasAccess
}

suspend fun shareCoachWithOthers(coachId: String, email: String): Boolean {
    val hasAccess = coaches.findOneById(coachId)?.hasAccess ?: return false
    return coaches.updateOneById(coachId, setValue(Coach::hasAccess, hasAccess + email)).wasAcknowledged()
}


suspend fun getCoachesSharedWIthMe(email: String): List<Coach> {
    return coaches.find(Coach::hasAccess contains email).toList()
}


suspend fun deleteCoach(coachId: String): Boolean {
    val coach = coaches.findOne(Coach::id eq coachId)
    coach?.let { coach ->
        return coaches.deleteOneById(coach.id).wasAcknowledged()
    } ?: return false
}

suspend fun removeCoachFromSharedWithMeList(coachId: String, email: String): Boolean {
    val coach_ = coaches.findOne(Coach::id eq coachId, Coach::hasAccess contains email)
    coach_?.let { coach_ ->
        val newHasAccess = coach_.hasAccess - email
        val updateResult = coaches.updateOne(Coach::id eq coach_.id, setValue(Coach::hasAccess, newHasAccess))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun getFavoriteCoach(parent: String): List<Coach> {
    return coaches.find(Coach::owner eq parent, Coach::favoriteStatus eq 1).toList()
}

suspend fun addCoachToFavorite(coachId: String): Boolean {
    val coach = coaches.findOne(Coach::id eq coachId, Coach::favoriteStatus ne 1)
    coach?.let { coach ->
        val newFavoriteStatus = 1
        val updateResult = coaches.updateOne(Coach::id eq coach.id, setValue(Coach::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun removeCoachFromFavorite(coachId: String): Boolean {
    val coach = coaches.findOne(Coach::id eq coachId, Coach::favoriteStatus ne 0)
    coach?.let { coach ->
        val newFavoriteStatus = 0
        val updateResult = coaches.updateOne(Coach::id eq coach.id, setValue(Coach::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}