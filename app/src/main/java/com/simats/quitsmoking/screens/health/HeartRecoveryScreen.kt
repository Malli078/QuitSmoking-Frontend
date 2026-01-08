package com.simats.quitsmoking.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.max
import kotlin.math.min

/* ---------- DATA MODEL ---------- */
private data class HeartMilestone(
    val days: Int,
    val time: String,
    val desc: String
)

/* ---------- REUSABLE WHITE CARD WITH SHADOW ---------- */
@Composable
fun WhiteCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

/* ---------- MAIN SCREEN ---------- */
@Composable
fun HeartRecoveryScreen(
    navController: NavController,
    quitDateMillis: Long?
) {
    val now = System.currentTimeMillis()
    val quitTs = quitDateMillis ?: now
    val msPerDay = 1000L * 60L * 60L * 24L

    val daysSinceQuit = max(0L, (now - quitTs) / msPerDay).toInt()
    val progressPercent = min(100.0, (daysSinceQuit.toDouble() / 365.0) * 100.0)
    val progressFraction = (progressPercent / 100.0).toFloat()

    val milestones = listOf(
        HeartMilestone(0, "20 Minutes", "Heart rate returns to normal ❤️"),
        HeartMilestone(1, "12 Hours", "Blood pressure normalizes 💧"),
        HeartMilestone(14, "2 Weeks", "Circulation improves 💪"),
        HeartMilestone(90, "3 Months", "Heart attack risk starts to drop 🩺"),
        HeartMilestone(365, "1 Year", "Heart disease risk cut in half 🌿"),
        HeartMilestone(1825, "5 Years", "Stroke risk reduced to non-smoker level 🌈")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFAF4))
    ) {

        /* ---------- HEADER (Fixed Alignment) ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFEF4444), Color(0xFFF87171))
                    )
                )
                .padding(top = 48.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Back button properly aligned
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Header Title & Subtitle beside back arrow
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Heart Recovery ❤️",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Your heart is getting stronger every day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        /* ---------- BODY CONTENT ---------- */
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            /* --- PROGRESS CARD --- */
            WhiteCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFE4E6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text("Heart Recovery Progress", color = Color.Gray)
                        Text(
                            "${progressPercent.toInt()}%",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF111827)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0xFFF1F5F9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .height(12.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFEF4444), Color(0xFFF87171))
                                )
                            )
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            /* --- INFO CARD --- */
            WhiteCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFE4E6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color(0xFFDC2626)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            "Amazing Progress!",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF111827)
                        )
                        Text(
                            "Your cardiovascular system is rapidly improving. Keep it up!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            /* --- MILESTONES --- */
            Text(
                "Recovery Milestones",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1E293B)
            )

            Spacer(Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                milestones.forEach { ms ->
                    val completed = daysSinceQuit >= ms.days

                    WhiteCard(shape = RoundedCornerShape(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (completed) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE5E7EB))
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text(
                                    ms.time,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (completed)
                                        Color(0xFF047857)
                                    else Color(0xFF111827)
                                )
                                Text(
                                    ms.desc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (completed)
                                        Color(0xFF10B981)
                                    else Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
