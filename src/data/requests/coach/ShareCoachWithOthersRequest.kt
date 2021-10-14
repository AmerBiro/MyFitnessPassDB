package com.androiddevs.data.requests.coach

data class ShareCoachWithOthersRequest(
    val coachId: String,
    val email: String,
    val owner: String
)