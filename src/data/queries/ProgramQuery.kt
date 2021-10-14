package com.androiddevs.data.queries

import com.noteapp.database.collections.Program
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programs = database.getCollection<Program>()

suspend fun createProgram(program: Program): Boolean {
    return programs.insertOne(program).wasAcknowledged()
}

suspend fun updateProgram(program: Program): Boolean {
    val programExists = programs.findOneById(program.id) != null
    if (programExists) {
        return programs.updateOneById(program.id, program).wasAcknowledged()
    }
    return false
}

suspend fun getPrograms(parent: String): List<Program> {
    return programs.find(Program::parent eq parent).toList()
}


suspend fun isProgramOwner(programId: String, owner: String): Boolean{
    val program = programs.findOneById(programId) ?: return false
    return program.owner == owner
}

suspend fun isProgramAlreadyShared(programId: String, email: String): Boolean{
    val program = programs.findOneById(programId)?: return true
    return email in program.hasAccess
}

suspend fun shareProgramWithOthers(programId: String, email: String): Boolean {
    val hasAccess = programs.findOneById(programId)?.hasAccess ?: return false
    return programs.updateOneById(programId, setValue(Program::hasAccess, hasAccess + email)).wasAcknowledged()
}



suspend fun getProgramsSharedWIthMe(email: String): List<Program> {
    return programs.find(Program::hasAccess contains email).toList()
}


suspend fun deleteProgram(programId: String): Boolean {
    val program = programs.findOne(Program::id eq programId)
    program?.let { program ->
        return programs.deleteOneById(program.id).wasAcknowledged()
    } ?: return false
}

suspend fun removeProgramFromSharedWithMeList(programId: String, email: String): Boolean {
    val program = programs.findOne(Program::id eq programId, Program::hasAccess contains email)
    program?.let { program ->
        val newHasAccess = program.hasAccess - email
        val updateResult = programs.updateOne(Program::id eq program.id, setValue(Program::hasAccess, newHasAccess))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun getFavoritePrograms(parent: String): List<Program> {
    return programs.find(Program::parent eq parent, Program::favoriteStatus eq 1).toList()
}

suspend fun addProgramToFavorite(programId: String): Boolean {
    val program = programs.findOne(Program::id eq programId, Program::favoriteStatus ne 1)
    program?.let { program ->
        val newFavoriteStatus = 1
        val updateResult = programs.updateOne(Program::id eq program.id, setValue(Program::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}

suspend fun removeProgramFromFavorite(programId: String): Boolean {
    val program = programs.findOne(Program::id eq programId, Program::favoriteStatus ne 0)
    program?.let { program ->
        val newFavoriteStatus = 0
        val updateResult = programs.updateOne(Program::id eq program.id, setValue(Program::favoriteStatus, newFavoriteStatus))
        return updateResult.wasAcknowledged()
    } ?: return false
}