package com.simats.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.quitsmoking.model.*
import com.simats.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _profile = MutableStateFlow<User?>(null)
    val profile: StateFlow<User?> = _profile

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProfile(userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.getProfile(userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _profile.value = response.body()?.profile
                } else {
                    _error.value = "Failed to load profile"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateProfile(
        userId: Int,
        name: String,
        email: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.updateProfile(
                    UpdateProfileRequest(userId, name, email)
                )
                if (response.isSuccessful && response.body()?.status == true) {
                    onSuccess()
                } else {
                    _error.value = response.body()?.message ?: "Update failed"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }
}
