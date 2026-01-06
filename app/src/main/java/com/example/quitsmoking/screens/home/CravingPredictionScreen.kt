// CravingPredictionScreen.kt
package com.example.quitsmoking.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

data class Prediction(
    val time: String,
    val risk: String,
    val reason: String,
    val confidence: Int
)

private val samplePredictions = listOf(
    Prediction(time = "11:30 AM - 12:00 PM", risk = "High", reason = "Coffee break (usual trigger)", confidence = 87),
    Prediction(time = "3:00 PM - 4:00 PM", risk = "Medium", reason = "Mid-afternoon stress pattern", confidence = 64),
    Prediction(time = "8:00 PM - 9:00 PM", risk = "Medium", reason = "After dinner routine", confidence = 72)
)

private fun riskColors(risk: String): Pair<Color, Color> {
    return when (risk) {
        "High" -> Color(0xFFDC2626) to Color(0xFFFEE2E2)     // red text / light red bg
        "Medium" -> Color(0xFFB45309) to Color(0xFFFFF7ED)   // amber text / light amber bg
        else -> Color(0xFF16A34A) to Color(0xFFECFDF5)       // green text / light green bg
    }
}

@Composable
fun CravingPredictionScreen(navController: NavController, predictions: List<Prediction> = samplePredictions) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) { // gray-50 like
        Column(modifier = Modifier.fillMaxSize()) {

            // Header gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF7C3AED), Color(0xFFEC4899)) // purple -> pink
                        )
                    )
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Column {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "AI Craving Prediction",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Stay ahead of your cravings",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Body content
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {

                // Info card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF3B82F6), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = "AI analysis",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(text = "AI Analysis", style = MaterialTheme.typography.titleMedium, color = Color(0xFF0F172A))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Based on your patterns, we've identified potential craving times for today.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }

                // Predictions header
                Text(
                    text = "Today's Predictions",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                // Predictions list
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    predictions.forEach { pred ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.AccessTime,
                                            contentDescription = "Time",
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = pred.time, color = Color(0xFF0F172A))
                                    }

                                    val (textColor, bgColor) = riskColors(pred.risk)
                                    Box(
                                        modifier = Modifier
                                            .background(color = bgColor, shape = RoundedCornerShape(50))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${pred.risk} Risk",
                                            color = textColor,
                                            style = MaterialTheme.typography.labelSmall,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(text = pred.reason, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF475569)))

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "Confidence", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))

                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        // progress bar
                                        Box(
                                            modifier = Modifier
                                                .width(120.dp)
                                                .height(8.dp)
                                                .background(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(6.dp))
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(pred.confidence.coerceIn(0, 100) / 100f)
                                                    .background(
                                                        brush = Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFFEC4899))),
                                                        shape = RoundedCornerShape(6.dp)
                                                    )
                                            )
                                        }

                                        Text(text = "${pred.confidence}%", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF0F172A))
                                    }
                                }
                            }
                        }
                    }
                }

                // Tip card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Tip",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Pro Tip", style = MaterialTheme.typography.titleMedium, color = Color(0xFF064E3B))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Set reminders 15 minutes before high-risk times to prepare your coping strategies.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CravingPredictionScreenPreview() {
    Surface {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Text("Preview of CravingPredictionScreen — open in app for full UI", modifier = Modifier.padding(8.dp))
        }
    }
}
