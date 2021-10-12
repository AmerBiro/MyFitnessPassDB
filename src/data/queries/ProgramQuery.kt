package com.androiddevs.data.queries

import com.androiddevs.data.collections.User
import com.noteapp.database.collections.Program
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("MyFitnessPassMongoDB")
private val programs = database.getCollection<Program>()
private val users = database.getCollection<User>()

suspend fun getOwnPrograms(owner: String): List<Program> {
    return programs.find(Program::owner eq owner).toList()
}

suspend fun getSharedProgramsWIthMe(email: String): List<Program> {
    return programs.find(Program::hasAccess contains email).toList()
}

suspend fun createUpdateProgram(program: Program): Boolean {
    val programExists = programs.findOneById(program.id) != null
    return if (programExists) {
        programs.updateOneById(program.id, program).wasAcknowledged()
    } else {
        programs.insertOne(program).wasAcknowledged()
    }
}

//suspend fun shareProgramWithOthers(programId: String, email: String): Boolean {
//    val shareProgram = programs.findOne(Program::id eq programId)
//    shareProgram?.let { shareProgram ->
//            // the note has multiple owners, so we just delete the email from the owners list
//            val newHasAccess = shareProgram.hasAccess + email
//            val updateResult = programs.updateOne(Program::id eq programId, setValue(Program::hasAccess, newHasAccess))
//            return updateResult.wasAcknowledged()
//
//    } ?: return false
//}

suspend fun deleteProgram(owner: String, programId: String): Boolean{
    val program = programs.findOne(Program::owner eq owner, Program::id eq  programId)
    program?.let { program ->
        return programs.deleteOneById(program.id).wasAcknowledged()
    } ?: return false
}
