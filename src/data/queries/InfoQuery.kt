package com.androiddevs.data.queries

import com.noteapp.database.collections.Info
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val info = database.getCollection<Info>()

suspend fun createInfo(info_: Info): Boolean {
    return info.insertOne(info_).wasAcknowledged()
}

suspend fun getInfo(owner: String): List<Info> {
    return info.find(Info::owner eq owner).toList()
}

suspend fun updateInfo(info_: Info): Boolean {
    val infoExists = info.findOneById(info_.id) != null
    if (infoExists) {
        return info.updateOneById(info_.id, info_).wasAcknowledged()
    }
    return false
}

