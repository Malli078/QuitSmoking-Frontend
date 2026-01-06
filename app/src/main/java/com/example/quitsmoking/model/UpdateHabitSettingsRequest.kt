package com.example.quitsmoking.model

data class UpdateHabitSettingsRequest(
    val user_id: Int,
    val cigarettes_per_day: Int,
    val cost_per_pack: Double,
    val quit_date: String?
)
