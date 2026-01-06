package com.example.quitsmoking.model

data class StatsResponse(
    val status: Boolean,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalSmokeFreeDays: Int
)
