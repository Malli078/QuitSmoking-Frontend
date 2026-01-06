package com.example.quitsmoking.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.max
import kotlin.math.min

private val TextBlack = Color(0xFF111827)

// ---------- White Card ----------
@Composable
fun AppWhiteCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

// ---------- Main Screen ----------
@Composable
fun EnergyImprovementScreen(
    navController: NavController,
    quitDateMillis: Long?
) {
    val now = System.currentTimeMillis()
    val quitTs = quitDateMillis ?: now
    val msPerDay = 1000L * 60 * 60 * 24
    val daysSinceQuit = max(0L, (now - quitTs) / msPerDay).toInt()
    val energyIncrease = min(100, daysSinceQuit * 2)

    var energyLevel by remember { mutableStateOf(7f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFF59E0B), Color(0xFFF97316))
                    )
                )
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }

            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text("Energy Levels", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Text("Feel the energy boost", color = Color.White.copy(alpha = 0.9f))
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // Progress Card
            AppWhiteCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box {
                        Box(
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.BatteryFull,
                                null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(64.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 16.dp, y = (-8).dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FlashOn, null, tint = Color.White)
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Text("Energy Improvement", color = TextBlack)
                    Text("+$energyIncrease%", style = MaterialTheme.typography.headlineMedium, color = TextBlack)

                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFEDE9F2))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(energyIncrease / 100f)
                                .height(10.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFF59E0B), Color(0xFFF97316))
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Info Card
            AppWhiteCard {
                Text(
                    "Why You Have More Energy",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextBlack
                )
                Spacer(Modifier.height(8.dp))
                Text("• Better oxygen circulation", color = TextBlack)
                Text("• Improved sleep quality", color = TextBlack)
                Text("• Normalized blood sugar levels", color = TextBlack)
                Text("• Reduced carbon monoxide in blood", color = TextBlack)
            }

            Spacer(Modifier.height(16.dp))

            // Slider Card
            AppWhiteCard {
                Text(
                    "Today's Energy Level",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextBlack
                )

                Spacer(Modifier.height(12.dp))

                Slider(
                    value = energyLevel,
                    onValueChange = { energyLevel = it.coerceIn(1f, 10f) },
                    valueRange = 1f..10f,
                    steps = 8
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Low", color = TextBlack)
                    Text("${energyLevel.toInt()}/10", color = TextBlack)
                    Text("High", color = TextBlack)
                }
            }
        }
    }
}
