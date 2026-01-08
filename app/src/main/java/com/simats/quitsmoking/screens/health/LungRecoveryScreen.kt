package com.simats.quitsmoking.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

private data class Milestone(val days: Int, val title: String, val desc: String)

@Composable
fun LungRecoveryScreen(navController: NavController, quitDateMillis: Long?) {

    val now = System.currentTimeMillis()
    val quitTs = quitDateMillis ?: now
    val msPerDay = 1000L * 60L * 60L * 24L

    val daysSinceQuit = max(0L, (now - quitTs) / msPerDay).toInt()
    val currentProgress = min(100.0, (daysSinceQuit / 365.0) * 100.0)
    val progressFraction = (currentProgress / 100.0).toFloat()

    val milestones = listOf(
        Milestone(0, "20 Minutes", "Heart rate drops to normal"),
        Milestone(1, "24 Hours", "Carbon monoxide levels normalize"),
        Milestone(3, "2–3 Days", "Breathing becomes easier"),
        Milestone(14, "2 Weeks", "Lung function improves by 30%"),
        Milestone(30, "1 Month", "Coughing & shortness of breath decrease"),
        Milestone(90, "3 Months", "Lung function increases significantly"),
        Milestone(365, "1 Year", "Cilia regrow inside lungs"),
        Milestone(1825, "5 Years", "Lung cancer risk cut in half")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {

        /* ---------- HEADER (Fixed Alignment) ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
                    )
                )
                .padding(top = 48.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Properly aligned Back button
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

                // Title and Subtitle aligned beside the back icon
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Lung Recovery",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Text(
                        "Your breathing is getting better",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
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

            // Progress Card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDBF9FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Air,
                                    contentDescription = null,
                                    tint = Color(0xFF0891B2)
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text("Days Smoke-Free", color = Color.Gray)
                                Text(
                                    daysSinceQuit.toString(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.Black
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Recovery", color = Color.Gray)
                            Text(
                                "${currentProgress.toInt()}%",
                                color = Color(0xFF06B6D4),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFE5E7EB))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressFraction)
                                .height(10.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF2563EB))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("You're Healing!", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                        Text(
                            "Your lungs are actively repairing themselves. Within 1 year, your lung function can improve by up to 30%.",
                            color = Color(0xFF6B7280),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Recovery Timeline", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Spacer(Modifier.height(8.dp))

            milestones.forEach { ms ->
                val completed = daysSinceQuit >= ms.days

                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (completed) Color(0xFFE6FFFA) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {

                        if (completed) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color(0xFFD1D5DB), CircleShape)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                ms.title,
                                color = if (completed) Color(0xFF065F46) else Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                ms.desc,
                                color = if (completed) Color(0xFF065F46) else Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
