package com.simats.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.quitsmoking.model.CalendarDay
import com.simats.quitsmoking.model.CalendarRequest
import com.simats.quitsmoking.model.StatsRequest
import com.simats.quitsmoking.model.StatsResponse
import com.simats.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StreakViewModel : ViewModel() {

    private val api = RetrofitClient.api

    private val _calendar = MutableStateFlow<List<CalendarDay>>(emptyList())
    val calendar: StateFlow<List<CalendarDay>> = _calendar

    private val _stats = MutableStateFlow<StatsResponse?>(null)
    val stats: StateFlow<StatsResponse?> = _stats

    fun loadMonth(userId: Int, year: Int, month: Int) {
        val monthStr = "%04d-%02d".format(year, month)

        viewModelScope.launch {
            try {
                val response = api.getStreakCalendar(
                    CalendarRequest(
                        user_id = userId,
                        month = monthStr
                    )
                )

                if (response.status) {
                    _calendar.value = response.calendar
                } else {
                    _calendar.value = emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _calendar.value = emptyList()
            }
        }
    }

    fun loadStats(userId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getStreakStats(
                    StatsRequest(user_id = userId)
                )
                _stats.value = response

            } catch (e: Exception) {
                e.printStackTrace()
                _stats.value = null
            }
        }
    }
}
