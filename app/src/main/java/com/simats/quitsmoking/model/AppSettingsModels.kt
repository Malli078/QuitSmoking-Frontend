package com.simats.quitsmoking.model

data class AppSettings(
    val dark_mode: Int,
    val notifications_enabled: Int,
    val sound_effects: Int,
    val haptic_feedback: Int
)

data class GetAppSettingsResponse(
    val status: Boolean,
    val settings: AppSettings
)

data class UpdateAppSettingsRequest(
    val user_id: Int,
    val dark_mode: Int,
    val notifications_enabled: Int,
    val sound_effects: Int,
    val haptic_feedback: Int
)
