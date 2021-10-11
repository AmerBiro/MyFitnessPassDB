package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Day(
    val number: Int,
    val name: String,
    val description: String,
    val color: String,
//    val note: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    @BsonId
    val id: String = ObjectId().toString()
)