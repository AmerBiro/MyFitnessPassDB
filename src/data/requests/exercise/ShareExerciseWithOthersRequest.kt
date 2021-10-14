package com.androiddevs.data.requests.exercise

data class ShareExerciseWithOthersRequest(
    val exerciseId: String,
    val email: String,
    val owner: String
)