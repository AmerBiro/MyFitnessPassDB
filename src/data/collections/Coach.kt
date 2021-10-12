package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Coach(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: Int,
    val website: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    val hasAccess: List<String>,
    val owner: String,
    val favoriteStatus: Int,
    @BsonId
    val id: String = ObjectId().toString()
)