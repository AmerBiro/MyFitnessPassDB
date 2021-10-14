package com.androiddevs.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class ProgramNote(
    val title: String,
    val content: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    val parent: String,
    val owner: String,
    @BsonId
    val id: String = ObjectId().toString()
)