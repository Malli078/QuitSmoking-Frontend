package com.simats.quitsmoking.screens.health

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
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
    val progress: Float,
    val status: String,
    val colorStart: Color,
    val colorEnd: Color,
    val route: String
)

/* -------------------- HELPERS -------------------- */

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
    val dur = Duration.between(quitInstant, now)
    return max(0L, dur.toDays())
}

private fun getHealthProgress(days: Double): Float {
    return min(100.0, (days / 365.0) * 100.0).toFloat()
}

@Composable
private fun whiteCardModifier(): Modifier {
    return Modifier
        .shadow(4.dp, RoundedCornerShape(14.dp))
        .clip(RoundedCornerShape(14.dp))
        .background(Color.White.copy(alpha = 0.95f))
}

/* -------------------- MAIN SCREEN -------------------- */

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
        /* -------------------- HEADER -------------------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF10B981), Color(0xFF059669))
                    )
                )
                .padding(top = 40.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            // Back Icon (aligned left)
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            // Centered Title
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Health Recovery",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Track your body's healing journey",
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }

        /* -------------------- BODY -------------------- */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            /* Overall Recovery */
            OverallRecoveryCard(overallRecovery, daysSinceQuit)

            /* Quick Actions Row */
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(
                    icon = Icons.Default.Add,
                    label = "Log Biometrics",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                ) { navController.navigate("biometrics_entry") }

                QuickActionCard(
                    icon = Icons.Default.TrendingUp,
                    label = "View Timeline",
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f)
                ) { navController.navigate("health_timeline") }
            }

            /* Featured Tools */
            FeaturedToolCard(
                title = "Recovery Graph",
                subtitle = "Visualize your 30-day progress",
                gradient = listOf(Color(0xFF8B5CF6), Color(0xFFF472B6))
            ) { navController.navigate("recovery-graph") }

            FeaturedToolCard(
                title = "Symptoms Better",
                subtitle = "Track your improvements",
                gradient = listOf(Color(0xFF10B981), Color(0xFF059669))
            ) { navController.navigate("symptoms-better") }

            FeaturedToolCard(
                title = "AI Health Prediction",
                subtitle = "See your personalized forecast",
                gradient = listOf(Color(0xFF6366F1), Color(0xFF7C3AED))
            ) { navController.navigate("ai-health-prediction") }

            /* Health Metrics */
            metrics.forEach { m ->
                MetricCard(m) { navController.navigate(m.route) }
            }

            /* Next Milestone */
            NextMilestoneCard(daysSinceQuit)

            Spacer(Modifier.height(16.dp))
        }
    }
}

/* -------------------- SUB-COMPOSABLES -------------------- */

@Composable
private fun OverallRecoveryCard(overallRecovery: Int, daysSinceQuit: Int) {
    Card(
        modifier = whiteCardModifier().fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Overall Recovery", fontWeight = FontWeight.Medium, color = Color(0xFF111827))
                    Spacer(Modifier.height(6.dp))
                    Text("${overallRecovery}%", style = MaterialTheme.typography.displaySmall, color = Color.Black)
                }
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(Color(0xFF34D399), Color(0xFF0EA5A3)))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = overallRecovery / 100f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.height(10.dp))
            Text("$daysSinceQuit days smoke-free • Keep going!", color = Color(0xFF1F2937))
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .background(Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = label, tint = color)
            Spacer(Modifier.height(6.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Black)
        }
    }
}

@Composable
private fun FeaturedToolCard(
    title: String,
    subtitle: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = whiteCardModifier().fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(gradient))
                .padding(16.dp)
        ) {
            Column {
                Text(title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
private fun MetricCard(m: HealthMetric, onClick: () -> Unit) {
    Card(
        modifier = whiteCardModifier()
            .fillMaxWidth()
            .clickable { onClick() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(listOf(m.colorStart, m.colorEnd))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(m.icon, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(m.label, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
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

@Composable
private fun NextMilestoneCard(daysSinceQuit: Int) {
    Card(
        modifier = whiteCardModifier().fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFFF472B6))))
                .padding(16.dp)
        ) {
            Column {
                Text("Next Health Milestone", color = Color.White.copy(alpha = 0.9f))
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
                Text(countdownText, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

/* -------------------- PREVIEW -------------------- */
@Preview(showBackground = true)
@Composable
private fun HealthDashboardPreview() {
    val nav = rememberNavController()
    val sampleInstant = Instant.now().minus(Duration.ofDays(10))
    val sampleUser = User(quitDateIso = sampleInstant.toString())
    HealthDashboardScreen(navController = nav, user = sampleUser)
}
