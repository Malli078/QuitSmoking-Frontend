// src/main/java/com/example/quitsmoking/screens/health/BiometricsEntryScreen.kt
package com.example.quitsmoking.screens.health

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Date

data class HealthMetrics(
    val bloodPressure: String,
    val pulse: Int,
    val spO2: Int,
    val timestamp: Date
)

@Composable
private fun whiteCardModifier(): Modifier {
    return Modifier
        .shadow(8.dp, RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White.copy(alpha = 0.9f))
}

/**
 * Material3-compatible Biometrics entry screen with white glass-style cards.
 * Routes and logic remain unchanged.
 */
@Composable
fun BiometricsEntryScreen(
    navController: NavController,
    addHealthMetrics: (HealthMetrics) -> Unit
) {
    val context = LocalContext.current

    var systolic by remember { mutableStateOf("120") }
    var diastolic by remember { mutableStateOf("80") }
    var pulse by remember { mutableStateOf("72") }
    var spO2 by remember { mutableStateOf("98") }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))) {
        // Manual top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 12.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Log Biometrics",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Track your vital signs",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Blood Pressure Card
            Card(
                modifier = whiteCardModifier().fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFCDD2).copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bloodtype,
                                contentDescription = "BP Icon",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFFEF4444)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Blood Pressure", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                            Text(
                                text = "Systolic / Diastolic",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = systolic,
                            onValueChange = { systolic = it.filterDigits() },
                            placeholder = { Text("120", color = Color(0xFF9CA3AF)) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "/", color = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = diastolic,
                            onValueChange = { diastolic = it.filterDigits() },
                            placeholder = { Text("80", color = Color(0xFF9CA3AF)) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "mmHg", color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pulse Card
            Card(
                modifier = whiteCardModifier().fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Pulse Icon",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF3B82F6)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Heart Rate", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                            Text(
                                text = "Beats per minute",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = pulse,
                            onValueChange = { pulse = it.filterDigits() },
                            placeholder = { Text("72", color = Color(0xFF9CA3AF)) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "bpm", color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SpO2 Card
            Card(
                modifier = whiteCardModifier().fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEDE9FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "SpO2 Icon",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF7C3AED)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Oxygen Saturation", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                            Text(
                                text = "Blood oxygen level",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = spO2,
                            onValueChange = {
                                val filtered = it.filterDigits()
                                spO2 = if (filtered.isNotBlank()) {
                                    val n = filtered.toIntOrNull() ?: 0
                                    n.coerceIn(0, 100).toString()
                                } else ""
                            },
                            placeholder = { Text("98", color = Color(0xFF9CA3AF)) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "%", color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(88.dp))
        }

        // Bottom Save Button
        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        val pPulse = pulse.toIntOrNull()
                        val pSpO2 = spO2.toIntOrNull()
                        val sSys = systolic.toIntOrNull()
                        val sDia = diastolic.toIntOrNull()

                        if (pPulse == null || pSpO2 == null || sSys == null || sDia == null) {
                            Toast.makeText(context, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val clampedSpO2 = pSpO2.coerceIn(0, 100)

                        addHealthMetrics(
                            HealthMetrics(
                                bloodPressure = "${sSys}/${sDia}",
                                pulse = pPulse,
                                spO2 = clampedSpO2,
                                timestamp = Date()
                            )
                        )

                        navController.navigate("health_dashboard")
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(text = "Save Metrics", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

/** Helper: keep digits only */
private fun String.filterDigits(): String = this.filter { it.isDigit() }
