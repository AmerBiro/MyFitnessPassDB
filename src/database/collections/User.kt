package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val gender: String,
    val birthdate: Long,
    val height: Double,
    val weight: Double,
    val imageURL: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    @BsonId
    val id: String = ObjectId().toString()
)