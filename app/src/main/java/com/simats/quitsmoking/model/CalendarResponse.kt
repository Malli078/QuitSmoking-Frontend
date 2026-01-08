package com.simats.quitsmoking.model

data class CalendarResponse(
    val status: Boolean,
    val calendar: List<CalendarDay>
)
