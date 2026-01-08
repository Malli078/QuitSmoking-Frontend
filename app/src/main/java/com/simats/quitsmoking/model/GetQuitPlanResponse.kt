package com.simats.quitsmoking.model

data class GetQuitPlanResponse(
    val status: Boolean,
    val quit_date: String?,
    val milestones: List<QuitMilestone>
)
