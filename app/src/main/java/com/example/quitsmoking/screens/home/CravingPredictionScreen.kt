// CravingPredictionScreen.kt
package com.example.quitsmoking.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Prediction(
    val time: String,
    val risk: String,
    val reason: String,
    val confidence: Int
)

private val samplePredictions = listOf(
    Prediction("11:30 AM - 12:00 PM", "High", "Coffee break (usual trigger)", 87),
    Prediction("3:00 PM - 4:00 PM", "Medium", "Mid-afternoon stress pattern", 64),
    Prediction("8:00 PM - 9:00 PM", "Medium", "After dinner routine", 72)
)

private fun riskColors(risk: String): Pair<Color, Color> =
    when (risk) {
        "High" -> Color(0xFFDC2626) to Color(0xFFFEE2E2)
        "Medium" -> Color(0xFFB45309) to Color(0xFFFFF7ED)
        else -> Color(0xFF16A34A) to Color(0xFFECFDF5)
    }

@Composable
fun CravingPredictionScreen(
    navController: NavController,
    predictions: List<Prediction> = samplePredictions
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF7C3AED), Color(0xFFEC4899))
                        )
                    )
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Column {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        "AI Craving Prediction",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Stay ahead of your cravings",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Info card (WHITE + BLACK TEXT)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF3B82F6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                "AI Analysis",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                            Text(
                                "Based on your patterns, we've identified potential craving times for today.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Today's Predictions",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    predictions.forEach { pred ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(Modifier.padding(14.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Filled.AccessTime,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(pred.time, color = Color.Black)
                                    }

                                    val (textColor, bgColor) = riskColors(pred.risk)
                                    Box(
                                        modifier = Modifier
                                            .background(bgColor, RoundedCornerShape(50))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "${pred.risk} Risk",
                                            color = textColor,
                                            style = MaterialTheme.typography.labelSmall,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    pred.reason,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )

                                Spacer(Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Confidence",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Black
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .width(120.dp)
                                                .height(8.dp)
                                                .background(
                                                    Color(0xFFF1F5F9),
                                                    RoundedCornerShape(6.dp)
                                                )
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(pred.confidence / 100f)
                                                    .background(
                                                        Brush.horizontalGradient(
                                                            listOf(
                                                                Color(0xFF7C3AED),
                                                                Color(0xFFEC4899)
                                                            )
                                                        ),
                                                        RoundedCornerShape(6.dp)
                                                    )
                                            )
                                        }

                                        Spacer(Modifier.width(8.dp))

                                        Text("${pred.confidence}%", color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Tip card (WHITE + BLACK TEXT)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = Color.Black
                        )

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(
                                "Pro Tip",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                            Text(
                                "Set reminders 15 minutes before high-risk times to prepare your coping strategies.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
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
        Text("Preview only")
    }
}
