package com.example.quitsmoking.model

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val password: String
)
