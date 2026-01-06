package com.example.quitsmoking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitsmoking.model.ForgotPasswordRequest
import com.example.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    val loading = mutableStateOf(false)
    val error = mutableStateOf("")
    val success = mutableStateOf(false)

    fun sendOtp(email: String) {

        if (email.isBlank()) {
            error.value = "Email is required"
            return
        }

        loading.value = true
        error.value = ""

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.forgotPassword(
                    ForgotPasswordRequest(email)
                )

                if (response.isSuccessful && response.body()?.status == true) {
                    success.value = true   // 🔥 triggers navigation
                } else {
                    error.value =
                        response.body()?.message ?: "Failed to send OTP"
                }

            } catch (e: Exception) {
                error.value = "Network error"
            } finally {
                loading.value = false
            }
        }
    }
}
