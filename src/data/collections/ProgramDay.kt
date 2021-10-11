package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class ProgramDay(
    val number: Int,
    val name: String,
    val description: String,
    val color: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    val ownersId: List<String>,
    @BsonId
    val id: String = ObjectId().toString()
)