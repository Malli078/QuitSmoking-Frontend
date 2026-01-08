package com.simats.quitsmoking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.quitsmoking.model.RegisterRequest
import com.simats.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    val loading = mutableStateOf(false)
    val error = mutableStateOf("")
    val success = mutableStateOf(false)

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        loading.value = true
        error.value = ""
        success.value = false

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.register(
                    RegisterRequest(
                        name = name,
                        email = email,
                        password = password
                    )
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        error.value = "Empty server response"
                    } else if (body.status) {
                        // ✅ SUCCESS — backend confirmed account creation
                        success.value = true
                    } else {
                        error.value = body.message
                    }
                } else {
                    error.value = "HTTP ${response.code()}"
                }
            } catch (e: Exception) {
                error.value = e.message ?: "Network error"
            } finally {
                loading.value = false
            }
        }
    }
}
