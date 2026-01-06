package com.example.quitsmoking.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitsmoking.model.LoginRequest
import com.example.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val loading = mutableStateOf(false)
    val error = mutableStateOf("")
    val success = mutableStateOf(false)

    /**
     * Login API
     * Saves user_id, name & email (IMPORTANT)
     */
    fun login(
        email: String,
        password: String,
        context: Context
    ) {
        loading.value = true
        error.value = ""
        success.value = false

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.login(
                    LoginRequest(email, password)
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        error.value = "Empty server response"

                    } else if (body.status && body.user != null) {

                        val user = body.user

                        // 🔥 SAVE USER ID (THIS FIXES HABIT + PROFILE)
                        val prefs = context.getSharedPreferences(
                            "user_profile",
                            Context.MODE_PRIVATE
                        )

                        prefs.edit()
                            .putInt("user_id", user.id)      // ✅ REQUIRED
                            .putString("name", user.name)
                            .putString("email", user.email)
                            .apply()

                        success.value = true

                    } else {
                        error.value = body.message
                    }

                } else {
                    error.value = "Server error (${response.code()})"
                }

            } catch (e: Exception) {
                error.value = e.message ?: "Network error"
            } finally {
                loading.value = false
            }
        }
    }
}
