// DailyProgressScreen.kt
package com.example.quitsmoking.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// simple model matching your JS data shape
data class CravingItem(
    val id: String,
    val timestampEpochMillis: Long,
    val overcome: Boolean
)

private val sampleCravings = listOf(
    CravingItem("c1", Instant.now().minusSeconds(60 * 60).toEpochMilli(), overcome = true),
    CravingItem("c2", Instant.now().minusSeconds(60 * 30).toEpochMilli(), overcome = false),
    CravingItem("c3", Instant.now().toEpochMilli(), overcome = true)
)

@Composable
fun DailyProgressScreen(
    navController: NavController,
    cravings: List<CravingItem> = sampleCravings
) {
    // filter cravings that happened today (local date)
    val todayCravings = remember(cravings) {
        val today = LocalDate.now(ZoneId.systemDefault())
        cravings.filter { c ->
            val cDate = Instant.ofEpochMilli(c.timestampEpochMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            cDate == today
        }
    }

    val overcome = todayCravings.count { it.overcome }
    val failed = todayCravings.size - overcome

    // stats - keep same structure as JS example
    val stats = listOf(
        Triple("Goals Completed", "5/8", Color(0xFF10B981)), // emerald
        Triple("Cravings Resisted", overcome.toString(), Color(0xFF3B82F6)), // blue
        Triple("Minutes Meditated", "15", Color(0xFF8B5CF6)), // purple
        Triple("Steps Taken", "8,432", Color(0xFFF59E0B)) // amber
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF7C3AED)))
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
                        text = "Daily Progress",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "How you did today",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Summary Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Today's Summary", style = MaterialTheme.typography.titleMedium, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            // Overcome
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFECFDF5))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Filled.CheckCircle, contentDescription = "Overcome", tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                                        Text("Overcome", color = Color(0xFF10B981), fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(overcome.toString(), style = MaterialTheme.typography.titleLarge)
                                }
                            }

                            // Struggled
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFF1F2))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Filled.Close, contentDescription = "Failed", tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                                        Text("Struggled", color = Color(0xFFDC2626), fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(failed.toString(), style = MaterialTheme.typography.titleLarge)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Grid
                Column(modifier = Modifier.fillMaxWidth()) {
                    val chunked = stats.chunked(2)
                    chunked.forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            row.forEach { stat ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(stat.first, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(stat.second, style = MaterialTheme.typography.titleMedium, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                            if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Achievements Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Brush.horizontalGradient(
                        listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5))
                    ).let { Color.White }) // keep subtle; gradient not directly supported as containerColor
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Trending", tint = Color.White, modifier = Modifier.size(20.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text("Great Job!", style = MaterialTheme.typography.titleMedium, color = Color(0xFF0F172A))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("You're 25% better than yesterday", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF374151))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Button
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Back to Home", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyProgressScreenPreview() {
    // preview with a no-op NavController: use a lambda wrapper in real app
    Surface {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Text("Preview - DailyProgressScreen", modifier = Modifier.padding(8.dp))
        }
    }
}
