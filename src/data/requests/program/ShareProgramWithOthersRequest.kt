package com.androiddevs.data.requests.program

data class ShareProgramWithOthersRequest(
    val programId: String,
    val email: String,
    val owner: String
)