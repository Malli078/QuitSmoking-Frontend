package com.example.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitsmoking.model.*
import com.example.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppSettingsViewModel : ViewModel() {

    private val _settings =
        MutableStateFlow<AppSettings?>(null)
    val settings: StateFlow<AppSettings?> = _settings

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSettings(userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response =
                    RetrofitClient.api.getAppSettings(userId)

                if (response.isSuccessful && response.body()?.status == true) {
                    _settings.value = response.body()?.settings
                } else {
                    _error.value = "Failed to load app settings"
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
        settings: AppSettings,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response =
                    RetrofitClient.api.updateAppSettings(
                        UpdateAppSettingsRequest(
                            userId,
                            settings.dark_mode,
                            settings.notifications_enabled,
                            settings.sound_effects,
                            settings.haptic_feedback
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
