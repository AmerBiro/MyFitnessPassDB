package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Coach(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: Int,
    @BsonId
    val id: String = ObjectId().toString()
)