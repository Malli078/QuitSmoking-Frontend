package com.example.quitsmoking.model

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val user: User? = null
)

data class User(
    val id: Int,
    val name: String,
    val email: String
)
