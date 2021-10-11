package com.androiddevs.data.queries

import com.androiddevs.data.collections.User
import com.noteapp.database.collections.Info
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val info = database.getCollection<Info>()

suspend fun getInfo(userId: String): List<Info> {
    return info.find(Info::ownersId contains userId).toList()
}

suspend fun createUpdateInfo(info_: Info): Boolean{
    val infoExists = info.findOneById(info_.id) != null
    return if (infoExists){
        info.updateOneById(info_.id, info_).wasAcknowledged()
    }else{
        info.insertOne(info_).wasAcknowledged()
    }
}