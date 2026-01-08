package com.simats.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrivacySecurityViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    /* ---------------- EXPORT USER DATA ---------------- */
    fun exportData(userId: Int) {
        if (userId <= 0) {
            _message.value = "Invalid user"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.exportUserData(userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _message.value = "Data exported successfully"
                } else {
                    _message.value = response.body()?.message ?: "Export failed"
                }
            } catch (e: Exception) {
                _message.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }

    /* ---------------- DELETE ACCOUNT ---------------- */
    fun deleteAccount(
        userId: Int,
        onSuccess: () -> Unit
    ) {
        if (userId <= 0) {
            _message.value = "Invalid user"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.deleteAccount(
                    mapOf("user_id" to userId) // ✅ FIXED HERE
                )
                if (response.isSuccessful && response.body()?.status == true) {
                    onSuccess()
                } else {
                    _message.value = response.body()?.message ?: "Delete failed"
                }
            } catch (e: Exception) {
                _message.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
