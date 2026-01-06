package com.example.quitsmoking.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val quitDate: String = "",
    val cigarettesPerDay: Int = 10,
    val costPerPack: Double = 10.0
)

class ProfileLocalViewModel(context: Context) : ViewModel() {

    private val prefs =
        context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    val state = mutableStateOf(loadProfile())

    private fun loadProfile(): ProfileState {
        return ProfileState(
            name = prefs.getString("name", "") ?: "",
            email = prefs.getString("email", "") ?: "",
            quitDate = prefs.getString("quitDate", "") ?: "",
            cigarettesPerDay = prefs.getInt("cigsPerDay", 10),
            costPerPack = prefs.getFloat("costPerPack", 10f).toDouble()
        )
    }

    fun saveProfile(
        name: String,
        email: String,
        quitDate: String,
        cigarettesPerDay: Int,
        costPerPack: Double
    ) {
        prefs.edit()
            .putString("name", name)
            .putString("email", email)
            .putString("quitDate", quitDate)
            .putInt("cigsPerDay", cigarettesPerDay)
            .putFloat("costPerPack", costPerPack.toFloat())
            .apply()

        state.value = loadProfile()
    }
}
