package com.simats.quitsmoking.model

data class HabitSettings(
    val cigarettes_per_day: Int,
    val cost_per_pack: Double,
    val quit_date: String?
)
