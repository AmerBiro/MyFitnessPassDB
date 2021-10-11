package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Sets(
    val number: Int,
    val weight: Double,
    val creationDate: Long,
    val lastUpdateDate: Long,
    val ownersId: List<String>,
    @BsonId
    val id: String = ObjectId().toString()
)