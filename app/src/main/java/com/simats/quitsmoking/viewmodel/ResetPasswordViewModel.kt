package com.simats.quitsmoking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.quitsmoking.model.ResetPasswordRequest
import com.simats.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {

    val loading = mutableStateOf(false)
    val error = mutableStateOf("")
    val success = mutableStateOf(false)

    fun reset(email: String, otp: String, password: String) {

        if (email.isBlank() || otp.isBlank() || password.isBlank()) {
            error.value = "All fields are required"
            return
        }

        if (password.length < 6) {
            error.value = "Password must be at least 6 characters"
            return
        }

        loading.value = true
        error.value = ""

        viewModelScope.launch {
            try {
                val res = RetrofitClient.api.resetPassword(
                    ResetPasswordRequest(email, otp, password)
                )
                if (res.isSuccessful && res.body()?.status == true) {
                    success.value = true
                } else {
                    error.value = res.body()?.message ?: "Reset failed"
                }
            } catch (e: Exception) {
                error.value = e.message ?: "Network error"
            } finally {
                loading.value = false
            }
        }
    }
}
