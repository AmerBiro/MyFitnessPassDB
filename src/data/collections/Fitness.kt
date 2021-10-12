package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Fitness(
    val fitnessName: String,
    val membershipId: String,
    val memberSince: Long,
    val phoneNumber: Int,
    val email: String,
    val website: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    val owner: String,
    @BsonId
    val id: String = ObjectId().toString()
)