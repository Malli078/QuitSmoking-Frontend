// src/main/java/com/example/quitsmoking/screens/health/HealthDashboardScreen.kt
package com.example.quitsmoking.screens.health

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.Duration
import java.time.Instant
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class User(val quitDateIso: String? = null)

data class HealthMetric(
    val id: String,
    val icon: ImageVector,
    val label: String,
    val progress: Float, // 0..100
    val status: String,
    val colorStart: Color,
    val colorEnd: Color,
    val route: String
)

private fun parseQuitInstantOrNow(iso: String?): Instant {
    if (iso == null) return Instant.now()
    return try {
        Instant.parse(iso)
    } catch (e: Exception) {
        try {
            Instant.ofEpochMilli(iso.toLong())
        } catch (_: Exception) {
            Instant.now()
        }
    }
}

private fun daysSince(quitInstant: Instant): Long {
    val now = Instant.now()
    val dur = java.time.Duration.between(quitInstant, now)
    return max(0L, dur.toDays())
}

private fun getHealthProgress(days: Double): Float {
    return min(100.0, (days / 365.0) * 100.0).toFloat()
}

@Composable
private fun whiteCardModifier(): Modifier {
    return Modifier
        .shadow(8.dp, RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White.copy(alpha = 0.9f))
}

@Composable
fun HealthDashboardScreen(
    navController: NavController,
    user: User?,
    modifier: Modifier = Modifier
) {
    val quitInstant = remember(user) { parseQuitInstantOrNow(user?.quitDateIso) }
    val daysSinceQuit = remember(quitInstant) { daysSince(quitInstant).toInt() }

    val metrics = remember(daysSinceQuit) {
        listOf(
            HealthMetric(
                id = "lung",
                icon = Icons.Default.Air,
                label = "Lung Capacity",
                progress = getHealthProgress(daysSinceQuit.toDouble()),
                status = when {
                    daysSinceQuit < 7 -> "Beginning recovery"
                    daysSinceQuit < 30 -> "Improving"
                    else -> "Great progress"
                },
                colorStart = Color(0xFF60A5FA),
                colorEnd = Color(0xFF3B82F6),
                route = "lung-recovery"
            ),
            HealthMetric(
                id = "heart",
                icon = Icons.Default.Favorite,
                label = "Heart Health",
                progress = getHealthProgress(daysSinceQuit * 1.2),
                status = if (daysSinceQuit < 2) "Normalizing" else "Improved circulation",
                colorStart = Color(0xFFFCA5A5),
                colorEnd = Color(0xFFEF4444),
                route = "heart-recovery"
            ),
            HealthMetric(
                id = "energy",
                icon = Icons.Default.BatteryFull,
                label = "Energy Levels",
                progress = getHealthProgress(daysSinceQuit * 1.5),
                status = if (daysSinceQuit < 14) "Increasing" else "Much better",
                colorStart = Color(0xFFFBBF24),
                colorEnd = Color(0xFFF97316),
                route = "energy_improvement"
            ),
            HealthMetric(
                id = "taste",
                icon = Icons.Default.SentimentSatisfied,
                label = "Taste & Smell",
                progress = getHealthProgress(daysSinceQuit * 2.0),
                status = if (daysSinceQuit < 3) "Starting to return" else "Restored",
                colorStart = Color(0xFF8B5CF6),
                colorEnd = Color(0xFF7C3AED),
                route = "taste-smell-recovery"
            )
        )
    }

    val overallRecovery = min(100, getHealthProgress(daysSinceQuit.toDouble()).roundToInt())
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {
        // Header gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF10B981), Color(0xFF059669))
                    )
                )
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp)
        ) {
            IconButton(onClick = { navController.navigate("home") }, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text("Health Recovery", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("Track your body's healing journey", color = Color.White.copy(alpha = 0.9f))
            }
        }

        // Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Overall Recovery Card
            Card(
                modifier = whiteCardModifier(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Overall Recovery", style = MaterialTheme.typography.labelMedium, color = Color(0xFF111827), fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("${overallRecovery}%", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold), color = Color.Black)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Brush.horizontalGradient(listOf(Color(0xFF34D399), Color(0xFF0EA5A3))))
                                .wrapContentSize(Alignment.Center)
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = overallRecovery / 100f,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("$daysSinceQuit days smoke-free • Keep going!", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1F2937))
                }
            }

            Spacer(Modifier.height(12.dp))

            // Quick Actions
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = whiteCardModifier().weight(1f).clickable { navController.navigate("biometrics_entry") },
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = "Log", tint = Color(0xFF10B981))
                        Spacer(Modifier.height(6.dp))
                        Text("Log Biometrics", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                    }
                }
                Card(
                    modifier = whiteCardModifier().weight(1f).clickable { navController.navigate("health_timeline") },
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.TrendingUp, contentDescription = "Timeline", tint = Color(0xFF3B82F6))
                        Spacer(Modifier.height(6.dp))
                        Text("View Timeline", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Featured Tools
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = whiteCardModifier().fillMaxWidth().clickable { navController.navigate("recovery-graph") },
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFF472B6))))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Recovery Graph", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(4.dp))
                            Text("Visualize your 30-day progress", color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }

                Card(
                    modifier = whiteCardModifier().fillMaxWidth().clickable { navController.navigate("symptoms-better") },
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF059669))))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Symptoms Better", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(4.dp))
                            Text("Track your improvements", color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }

                Card(
                    modifier = whiteCardModifier().fillMaxWidth().clickable { navController.navigate("ai-health-prediction") },
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFF7C3AED))))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("AI Health Prediction", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(4.dp))
                            Text("See your personalized forecast", color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Health Metrics
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                metrics.forEach { m ->
                    Card(
                        modifier = whiteCardModifier().fillMaxWidth().clickable { navController.navigate(m.route) }.animateContentSize(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Brush.horizontalGradient(listOf(m.colorStart, m.colorEnd))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(m.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(m.label, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                                    Spacer(Modifier.height(2.dp))
                                    Text(m.status, style = MaterialTheme.typography.bodySmall, color = Color(0xFF4B5563))
                                }
                                Text("${m.progress.roundToInt()}%", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                            }
                            Spacer(Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = (m.progress / 100f).coerceIn(0f, 1f),
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(6.dp))
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Next Milestone
            Card(
                modifier = whiteCardModifier().fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFFF472B6))))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Next Health Milestone", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.9f))
                        Spacer(Modifier.height(6.dp))
                        val milestoneText = when {
                            daysSinceQuit < 1 -> "20 minutes: Heart rate normalizes"
                            daysSinceQuit < 3 -> "72 hours: Breathing easier"
                            daysSinceQuit < 14 -> "2 weeks: Circulation improves"
                            daysSinceQuit < 90 -> "3 months: Lung function +30%"
                            else -> "1 year: Heart disease risk cut in half!"
                        }
                        Text(milestoneText, style = MaterialTheme.typography.titleMedium, color = Color.White)
                        val countdownText = when {
                            daysSinceQuit < 1 -> "In about 20 minutes"
                            daysSinceQuit < 3 -> "In ${3 - daysSinceQuit} days"
                            daysSinceQuit < 14 -> "In ${14 - daysSinceQuit} days"
                            daysSinceQuit < 90 -> "In ${90 - daysSinceQuit} days"
                            else -> "In ${365 - daysSinceQuit} days"
                        }
                        Text(countdownText, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HealthDashboardPreview() {
    val nav = rememberNavController()
    val sampleInstant = Instant.now().minus(Duration.ofDays(10))
    val sampleUser = User(quitDateIso = sampleInstant.toString())
    HealthDashboardScreen(navController = nav, user = sampleUser)
}
