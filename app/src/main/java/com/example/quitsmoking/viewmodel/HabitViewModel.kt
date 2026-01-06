package com.example.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitsmoking.model.*
import com.example.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HabitViewModel : ViewModel() {

    private val _habits = MutableStateFlow<HabitSettings?>(null)
    val habits: StateFlow<HabitSettings?> = _habits

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadHabits(userId: Int) {
        if (userId <= 0) {
            _error.value = "Invalid user id"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.getHabitSettings(userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _habits.value = response.body()?.habits
                } else {
                    _error.value = "Failed to load habits"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveHabits(
        userId: Int,
        cigarettesPerDay: Int,
        costPerPack: Double,
        quitDate: String?,
        onSuccess: () -> Unit
    ) {
        if (userId <= 0) {
            _error.value = "Invalid user id"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.updateHabitSettings(
                    UpdateHabitSettingsRequest(
                        user_id = userId,
                        cigarettes_per_day = cigarettesPerDay,
                        cost_per_pack = costPerPack,
                        quit_date = quitDate
                    )
                )

                if (response.isSuccessful && response.body()?.status == true) {
                    onSuccess()
                } else {
                    _error.value = response.body()?.message ?: "Save failed"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }
}
