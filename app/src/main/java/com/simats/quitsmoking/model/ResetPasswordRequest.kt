package com.simats.quitsmoking.model

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val password: String
)
