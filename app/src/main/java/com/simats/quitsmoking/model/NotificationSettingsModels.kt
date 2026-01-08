package com.simats.quitsmoking.model

data class NotificationSettings(
    val push_enabled: Int,
    val milestone_enabled: Int,
    val daily_reminder_enabled: Int,
    val email_enabled: Int,
    val sound_enabled: Int,
    val vibrate_enabled: Int,
    val dnd_start: String,
    val dnd_end: String
)

data class GetNotificationSettingsResponse(
    val status: Boolean,
    val settings: NotificationSettings
)

data class UpdateNotificationSettingsRequest(
    val user_id: Int,
    val push_enabled: Int,
    val milestone_enabled: Int,
    val daily_reminder_enabled: Int,
    val email_enabled: Int,
    val sound_enabled: Int,
    val vibrate_enabled: Int,
    val dnd_start: String,
    val dnd_end: String
)
