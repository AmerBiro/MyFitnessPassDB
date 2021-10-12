package com.androiddevs.data.requests.program

data class DeleteProgramRequest(
    val owner: String,
    val programId: String
)