package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Program(
    val programName: String,
    val description: String,
    val days: Int,
    val fitness: String,
    val coach: String,
    val creationDate: Long,
    val lastUpdateDate: Long,
    val startDate: Long,
    val endDate: Long,
    val owner: String,
    val hasAccess: List<String>,
    val imageURL: String,
    val favoriteStatus: Int,
    @BsonId
    val id: String = ObjectId().toString()
)