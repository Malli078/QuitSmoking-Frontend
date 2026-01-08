package com.simats.quitsmoking.screens.home

import androidx.lifecycle.ViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class CravingItem(
    val id: String,
    val timestampEpochMillis: Long,
    val overcome: Boolean
)

class DailyProgressViewModel : ViewModel() {

    // TEMP demo data — replace with Room later
    private val allCravings = listOf(
        CravingItem("1", nowMinusHours(2), true),
        CravingItem("2", nowMinusHours(1), false),
        CravingItem("3", nowMinusDays(1, 3), true),
        CravingItem("4", nowMinusDays(1, 1), false)
    )

    fun todayCravings(): List<CravingItem> =
        filterByDate(LocalDate.now())

    fun yesterdayCravings(): List<CravingItem> =
        filterByDate(LocalDate.now().minusDays(1))

    private fun filterByDate(date: LocalDate): List<CravingItem> =
        allCravings.filter {
            Instant.ofEpochMilli(it.timestampEpochMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate() == date
        }

    companion object {
        private fun nowMinusHours(h: Long) =
            Instant.now().minusSeconds(h * 3600).toEpochMilli()

        private fun nowMinusDays(d: Long, h: Long) =
            Instant.now().minusSeconds(d * 86400 + h * 3600).toEpochMilli()
    }
}
