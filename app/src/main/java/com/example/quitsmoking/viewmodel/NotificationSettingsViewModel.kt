package com.example.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitsmoking.model.*
import com.example.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationSettingsViewModel : ViewModel() {

    private val _settings =
        MutableStateFlow<NotificationSettings?>(null)
    val settings: StateFlow<NotificationSettings?> = _settings

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSettings(userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response =
                    RetrofitClient.api.getNotificationSettings(userId)

                if (response.isSuccessful && response.body()?.status == true) {
                    _settings.value = response.body()?.settings
                } else {
                    _error.value = "Failed to load notification settings"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveSettings(
        userId: Int,
        settings: NotificationSettings,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response =
                    RetrofitClient.api.updateNotificationSettings(
                        UpdateNotificationSettingsRequest(
                            userId,
                            settings.push_enabled,
                            settings.milestone_enabled,
                            settings.daily_reminder_enabled,
                            settings.email_enabled,
                            settings.sound_enabled,
                            settings.vibrate_enabled,
                            settings.dnd_start,
                            settings.dnd_end
                        )
                    )

                if (response.isSuccessful && response.body()?.status == true) {
                    onSuccess()
                } else {
                    _error.value = "Failed to save settings"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }
}
