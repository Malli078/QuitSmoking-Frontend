package com.simats.quitsmoking.screens.health

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException
import kotlin.math.min
import kotlin.math.roundToInt
import kotlinx.coroutines.delay

/* ---------------- DATA ---------------- */

private data class Prediction(
    val metric: String,
    val current: Float,
    val predicted: Float,
    val timeframe: String,
    val colorKey: String,
    val iconType: IconType
)

private enum class IconType { HEART, LUNG, BATTERY, TREND }

/* ---------------- WHITE CARD ---------------- */

@Composable
fun WhiteCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(content = content)
    }
}

/* ---------------- MAIN SCREEN ---------------- */

@Composable
fun AIHealthPredictionScreen(navController: NavController, user: User?) {
    val quitInstant = remember(user) { parseQuitInstantOrNow(user?.quitDateIso) }
    val daysSinceQuit = remember(quitInstant) {
        Duration.between(quitInstant, Instant.now()).toDays().coerceAtLeast(0L).toInt()
    }

    val predictions = remember(daysSinceQuit) {
        listOf(
            Prediction("Heart Health", min(70f + daysSinceQuit * 0.5f, 95f), min(80f + daysSinceQuit * 0.5f, 100f), "30 days", "rose", IconType.HEART),
            Prediction("Lung Function", min(60f + daysSinceQuit * 0.7f, 90f), min(75f + daysSinceQuit * 0.7f, 100f), "60 days", "cyan", IconType.LUNG),
            Prediction("Energy Levels", min(65f + daysSinceQuit * 0.6f, 92f), min(85f + daysSinceQuit * 0.6f, 100f), "45 days", "amber", IconType.BATTERY)
        )
    }

    val colorMap = mapOf(
        "rose" to Pair(Color(0xFFFFE4E6), Color(0xFFBE123C)),
        "cyan" to Pair(Color(0xFFE6FFFA), Color(0xFF0891B2)),
        "amber" to Pair(Color(0xFFFFFBEB), Color(0xFFB45309))
    )

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(targetValue = if (startAnimation) 1f else 0f, animationSpec = tween(800))
    LaunchedEffect(Unit) { startAnimation = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .verticalScroll(rememberScrollState())
            .alpha(alphaAnim)
    ) {

        /* ---------- HEADER (Proper Alignment) ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF6366F1), Color(0xFF7C3AED))
                    )
                )
                .padding(top = 48.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Properly aligned back button (slightly below top edge)
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

                // Title + Subtitle aligned with the arrow
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "AI Health Prediction",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Text(
                        "Your personalized forecast",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        /* ---------- INTRO CARD ---------- */
        WhiteCard(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = "AI",
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("AI Analysis", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Based on your progress and thousands of quitters’ data, here’s what to expect.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B5563)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        /* ---------- PREDICTION CARDS ---------- */
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            predictions.forEachIndexed { index, p ->
                val visible = remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 150L)
                    visible.value = true
                }

                AnimatedVisibility(visible = visible.value) {
                    val colors = colorMap[p.colorKey] ?: Pair(Color.LightGray, Color.DarkGray)
                    WhiteCard {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(colors.first),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (p.iconType) {
                                        IconType.HEART -> Icon(Icons.Default.Favorite, contentDescription = null, tint = colors.second)
                                        IconType.LUNG -> Icon(Icons.Default.TrendingUp, contentDescription = null, tint = colors.second)
                                        IconType.BATTERY -> Icon(Icons.Default.BatteryFull, contentDescription = null, tint = colors.second)
                                        IconType.TREND -> Icon(Icons.Default.TrendingUp, contentDescription = null, tint = colors.second)
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(p.metric, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                                    Spacer(Modifier.height(2.dp))
                                    Text("Predicted in ${p.timeframe}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                                }
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF10B981))
                            }

                            Spacer(Modifier.height(12.dp))

                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Current", style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                                Text("${p.current.roundToInt()}%", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                            }
                            Spacer(Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = (p.current.coerceIn(0f, 100f) / 100f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                trackColor = Color(0xFFF1F5F9)
                            )

                            Spacer(Modifier.height(12.dp))

                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Predicted", style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                                Text("${p.predicted.roundToInt()}%", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF10B981))
                            }
                            Spacer(Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = (p.predicted.coerceIn(0f, 100f) / 100f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Color(0xFF10B981),
                                trackColor = Color(0xFFF1F5F9)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ---------- TIPS CARD ---------- */
        WhiteCard(modifier = Modifier.padding(horizontal = 16.dp)) {
            Column {
                Text("To Maximize Recovery", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        "• Stay consistent with your quit journey",
                        "• Exercise regularly to boost lung health",
                        "• Stay hydrated (8+ glasses daily)",
                        "• Get 7–9 hours of quality sleep",
                        "• Practice breathing exercises daily"
                    ).forEach {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = Color(0xFF4B5563))
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

/* ---------------- HELPER ---------------- */
private fun parseQuitInstantOrNow(iso: String?): Instant {
    if (iso == null) return Instant.now()
    return try {
        Instant.parse(iso)
    } catch (e: DateTimeParseException) {
        try {
            Instant.ofEpochMilli(iso.toLong())
        } catch (_: Exception) {
            Instant.now()
        }
    }
}
