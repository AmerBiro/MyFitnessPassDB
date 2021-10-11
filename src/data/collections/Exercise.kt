package com.noteapp.database.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Exercise(
     val number: Int,
     val name: String,
     val description: String,
//    val note: String,
     val set: Int,
     val reps: Int,
     val rest: Int,
     val weight: Double,
     val imageURL: String,
     val videoURL: String,
     val creationDate: Long,
     val lastUpdateDate: Long,
     @BsonId
     val id: String = ObjectId().toString()
)