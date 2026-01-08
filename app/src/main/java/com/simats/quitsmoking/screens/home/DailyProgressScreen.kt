package com.simats.quitsmoking.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun DailyProgressScreen(
    navController: NavController,
    viewModel: DailyProgressViewModel = viewModel()
) {
    val today = viewModel.todayCravings()
    val yesterday = viewModel.yesterdayCravings()

    val todayOvercome = today.count { it.overcome }
    val todayFailed = today.size - todayOvercome
    val yesterdayOvercome = yesterday.count { it.overcome }

    val improvementPercent =
        if (yesterdayOvercome <= 0) 100
        else ((todayOvercome - yesterdayOvercome) * 100 / yesterdayOvercome)

    // Stats grid (partially dynamic, placeholders marked)
    val stats = listOf(
        "Cravings Resisted" to todayOvercome.toString(),
        "Cravings Failed" to todayFailed.toString(),
        "Sessions Logged" to today.size.toString(),
        "Streak (days)" to "—" // TODO: hook to streak logic
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) {
        Column {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6366F1), Color(0xFF7C3AED))
                        )
                    )
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Column {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }

                    Text(
                        "Daily Progress",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Automatically updates every day",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Today's Summary", color = Color.Black)

                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            SummaryBox(
                                "Overcome",
                                todayOvercome,
                                Color(0xFFECFDF5),
                                Icons.Filled.CheckCircle,
                                Color(0xFF10B981)
                            )

                            SummaryBox(
                                "Struggled",
                                todayFailed,
                                Color(0xFFFFF1F2),
                                Icons.Filled.Close,
                                Color(0xFFDC2626)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Stats Grid (RESTORED)
                stats.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { stat ->
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(Color.White),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(stat.first, color = Color.Black, fontSize = 12.sp)
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        stat.second,
                                        color = Color.Black,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Achievement Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = Color.White)
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text("Great Job!", color = Color.Black)
                            Text(
                                "You're $improvementPercent% better than yesterday",
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Back to Home", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun RowScope.SummaryBox(
    label: String,
    value: Int,
    bg: Color,
    icon: ImageVector,
    iconColor: Color
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(label, fontSize = 12.sp, color = Color.Black)
            }
            Spacer(Modifier.height(8.dp))
            Text(value.toString(), color = Color.Black, style = MaterialTheme.typography.titleLarge)
        }
    }
}
