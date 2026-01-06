package com.example.quitsmoking.model

data class GetHabitSettingsResponse(
    val status: Boolean,
    val habits: HabitSettings?
)
