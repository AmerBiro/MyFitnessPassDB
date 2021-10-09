package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Set(
    val number: Int,
    val weight: Double,
    @BsonId
    val id: String = ObjectId().toString()
)