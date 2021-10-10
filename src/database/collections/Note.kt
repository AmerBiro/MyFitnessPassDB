package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Note(
    val content: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    @BsonId
    val id: String = ObjectId().toString()
)