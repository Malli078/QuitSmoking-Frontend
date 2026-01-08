package com.simats.quitsmoking.model

data class UpdateProfileRequest(
    val user_id: Int,
    val name: String,
    val email: String
)
