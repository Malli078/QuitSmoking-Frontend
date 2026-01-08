package com.simats.quitsmoking.model

data class GetProfileResponse(
    val status: Boolean,
    val profile: User?
)
