package com.example.quitsmoking.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.floor

@Composable
fun HealthTimelineScreen(navController: NavController, quitDate: Long?) {

    val daysSinceQuit = floor(
        (System.currentTimeMillis() - (quitDate ?: System.currentTimeMillis())) /
                (1000 * 60 * 60 * 24f)
    ).toInt()

    val timeline = listOf(
        Triple("20 min", "Heart Rate Normalizes", 0),
        Triple("12 hours", "Carbon Monoxide Clears", 0),
        Triple("24 hours", "Heart Attack Risk Drops", 1),
        Triple("48 hours", "Nerve Endings Regrow", 2),
        Triple("72 hours", "Breathing Easier", 3),
        Triple("2 weeks", "Circulation Improves", 14),
        Triple("1 month", "Lung Function +30%", 30),
        Triple("3 months", "Coughing Decreases", 90),
        Triple("9 months", "Cilia Regrow", 270),
        Triple("1 year", "Heart Disease Risk Halved", 365),
        Triple("5 years", "Stroke Risk Normalized", 1825),
        Triple("10 years", "Lung Cancer Risk Halved", 3650)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {
        // Header Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF14B8A6), Color(0xFF06B6D4))
                    )
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Column(modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)) {
                Text("Health Timeline", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                Text("Your recovery journey", color = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            // Days Smoke-Free Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text("Days Smoke-Free", color = Color.Gray)
                    Text(daysSinceQuit.toString(), style = MaterialTheme.typography.displaySmall)
                }
            }

            // Timeline Items
            timeline.forEachIndexed { index, item ->
                val (time, title, days) = item
                val isComplete = daysSinceQuit >= days

                Row(modifier = Modifier.padding(bottom = 20.dp)) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isComplete) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE5E7EB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }

                        if (index < timeline.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(40.dp)
                                    .background(if (isComplete) Color(0xFF10B981) else Color(0xFFE5E7EB))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(time, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                        Text(title, color = Color.Black)
                    }
                }
            }
        }
    }
}
