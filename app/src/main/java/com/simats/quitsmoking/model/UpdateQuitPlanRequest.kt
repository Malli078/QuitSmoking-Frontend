package com.simats.quitsmoking.model

data class UpdateQuitPlanRequest(
    val user_id: Int,
    val quit_date: String?,
    val milestones: List<QuitMilestone>
)
