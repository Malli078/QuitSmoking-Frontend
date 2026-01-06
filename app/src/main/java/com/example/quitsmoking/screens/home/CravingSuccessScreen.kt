package com.example.quitsmoking.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

private data class StatItem(val label: String, val value: String, val icon: ImageVector, val color: Color)

@Composable
fun CravingSuccessScreen(navController: NavController) {
    // show confetti for 3 seconds
    var showConfetti by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000L)
        showConfetti = false
    }

    val stats = listOf(
        StatItem("Cravings Overcome", "12", Icons.Default.EmojiEvents, Color(0xFFFFD54F)), // amber
        StatItem("Cigarettes Avoided", "120", Icons.Default.TrendingUp, Color(0xFF10B981)), // emerald
        StatItem("Money Saved", "$60", Icons.Default.AttachMoney, Color(0xFF16A34A)), // green
        StatItem("Health Improving", "100%", Icons.Default.Favorite, Color(0xFFEF4444)) // red
    )

    // Precompute random positions and colors for confetti dots
    val confettiCount = 30
    val confettiPositions = remember {
        List(confettiCount) {
            Pair(Random.nextFloat(), Random.nextFloat())
        }
    }
    val confettiColors = listOf(Color(0xFFFBBF24), Color(0xFF10B981), Color(0xFF3B82F6), Color(0xFFEC4899))

    // Animate overall confetti alpha for a subtle fade out
    val confettiAlpha by animateFloatAsState(targetValue = if (showConfetti) 1f else 0f)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF059669) // teal / emerald-ish background approximation
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Confetti layer (absolute)
            if (confettiAlpha > 0f) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                    confettiPositions.forEachIndexed { idx, pos ->
                        val x = pos.first
                        val y = pos.second
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = (x * 100).dp,
                                    y = (y * 100).dp
                                )
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(confettiColors[idx % confettiColors.size].copy(alpha = confettiAlpha))
                        )
                    }
                }
            }

            // Main content (on top)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Trophy circle
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Craving Overcome! 🎉",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You did it! You're stronger than your cravings.",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stats grid: 2 columns
                Column(modifier = Modifier.fillMaxWidth()) {
                    val pairs = stats.chunked(2)
                    pairs.forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            row.forEach { stat ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(100.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.White.copy(alpha = 0.12f),
                                    tonalElevation = 0.dp
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        // Icon bubble
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = stat.icon,
                                                contentDescription = stat.label,
                                                tint = stat.color,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(text = stat.value, color = Color.White, style = MaterialTheme.typography.headlineSmall)
                                        Text(text = stat.label, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                            if (row.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Motivational message card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "\"Every craving you resist makes you stronger. Your body is healing right now!\" 💪",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Streak chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔥", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Current Streak", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
                        Text(text = "Keep going strong!", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Action buttons bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF10B981))
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Back to Home")
                }

                OutlinedButton(
                    onClick = { navController.navigate("achievements") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40.dp),
                    border = ButtonDefaults.outlinedButtonBorder,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = "Achievements", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "View Achievements")
                }
            }
        }
    }
}
