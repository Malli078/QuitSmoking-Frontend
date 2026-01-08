package com.simats.quitsmoking.screens.health

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.floor
import kotlin.math.min

private val TextBlack = Color(0xFF111827)
private val TextMuted = Color(0xFF6B7280)

@Composable
fun TasteSmellRecoveryScreen(
    navController: NavController,
    quitDateMillis: Long?
) {
    val now = System.currentTimeMillis()
    val quit = quitDateMillis ?: now
    val daysSinceQuit = floor((now - quit) / (1000 * 60 * 60 * 24f))
        .toInt()
        .coerceAtLeast(0)

    val recoveryPercent = min((daysSinceQuit / 30.0 * 100.0).toInt(), 100)

    data class Experience(
        val icon: ImageVector,
        val label: String,
        val unlocked: Boolean
    )

    val experiences = listOf(
        Experience(Icons.Default.LocalCafe, "Coffee aroma", daysSinceQuit >= 2),
        Experience(Icons.Default.Fastfood, "Food flavors", daysSinceQuit >= 3),
        Experience(Icons.Default.LocalFlorist, "Fresh flowers", daysSinceQuit >= 5),
        Experience(Icons.Default.TagFaces, "Full taste & smell", daysSinceQuit >= 30)
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
                        listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))
                    )
                )
                .padding(top = 48.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Properly aligned back button
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 2.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Header title + subtitle beside back arrow
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Taste & Smell 👃",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Your senses are returning",
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

            // ---------- Progress Card ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFF3E8FF), Color(0xFFFDE8F0))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.TagFaces,
                            null,
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text("Senses Recovery", color = TextMuted)
                    Text(
                        "$recoveryPercent%",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextBlack
                    )

                    Spacer(Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = recoveryPercent / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF8B5CF6),
                        trackColor = Color(0xFFE5E7EB)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---------- Info Card ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        "Good News!",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextBlack
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Your taste buds and smell receptors regenerate within 48 hours. Full recovery takes about 1 month.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Experiences Unlocked",
                style = MaterialTheme.typography.titleMedium,
                color = TextBlack
            )

            Spacer(Modifier.height(12.dp))

            // ---------- Experiences Grid ----------
            experiences.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { exp ->
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .border(
                                        BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Box(modifier = Modifier.height(36.dp)) {
                                    Icon(
                                        exp.icon,
                                        null,
                                        tint = if (exp.unlocked) Color(0xFF059669) else Color(0xFF9CA3AF),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    if (exp.unlocked) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier
                                                .size(18.dp)
                                                .align(Alignment.TopEnd)
                                        )
                                    }
                                }

                                Spacer(Modifier.height(10.dp))
                                Text(
                                    exp.label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (exp.unlocked) TextBlack else TextMuted
                                )
                            }
                        }
                    }

                    if (rowItems.size < 2) Spacer(Modifier.weight(1f))
                }

                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
