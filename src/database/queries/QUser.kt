package com.noteapp.database.queries

import com.noteapp.database.collections.User
import io.ktor.html.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPass")
private val users = database.getCollection<User>()

suspend fun registerUser(user: User): Boolean{
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean{
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, passwordToBeChecked: String): Boolean{
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return actualPassword == passwordToBeChecked
}


