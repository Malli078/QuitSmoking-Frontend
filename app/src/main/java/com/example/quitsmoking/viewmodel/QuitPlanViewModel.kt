package com.example.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitsmoking.model.*
import com.example.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuitPlanViewModel : ViewModel() {

    private val _quitDate = MutableStateFlow<String?>(null)
    val quitDate: StateFlow<String?> = _quitDate

    private val _milestones =
        MutableStateFlow<List<QuitMilestone>>(emptyList())
    val milestones: StateFlow<List<QuitMilestone>> = _milestones

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadQuitPlan(userId: Int) {
        if (userId <= 0) {
            _error.value = "Invalid user id"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.getQuitPlan(userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _quitDate.value = response.body()?.quit_date
                    _milestones.value = response.body()?.milestones ?: emptyList()
                } else {
                    _error.value = "Failed to load quit plan"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveQuitPlan(
        userId: Int,
        quitDate: String?,
        milestones: List<QuitMilestone>,
        onSuccess: () -> Unit
    ) {
        if (userId <= 0) {
            _error.value = "Invalid user id"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.updateQuitPlan(
                    UpdateQuitPlanRequest(
                        user_id = userId,
                        quit_date = quitDate,
                        milestones = milestones
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
