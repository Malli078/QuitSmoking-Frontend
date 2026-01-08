package com.simats.quitsmoking.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class SuggestionItem(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val duration: String,
    val colorKey: String,
    val path: String? = null
)

@Composable
fun StressSuggestionsScreen(navController: NavController) {

    val initial = listOf(
        SuggestionItem(
            icon = Icons.Filled.Air,
            title = "4-7-8 Breathing",
            description = "Calm your nervous system in 2 minutes",
            duration = "2 min",
            colorKey = "blue",
            path = "/breathing-exercise"
        ),
        SuggestionItem(
            icon = Icons.Filled.MusicNote,
            title = "Calming Music",
            description = "Listen to stress-relief sounds",
            duration = "10 min",
            colorKey = "purple"
        ),
        SuggestionItem(
            icon = Icons.Filled.LocalCafe,
            title = "Take a Break",
            description = "Step away for a mindful moment",
            duration = "5 min",
            colorKey = "amber"
        ),
        SuggestionItem(
            // FIXED: Use AutoMirrored version (not deprecated)
            icon = Icons.AutoMirrored.Filled.MenuBook,
            title = "Read Motivation",
            description = "Quick inspirational quote",
            duration = "1 min",
            colorKey = "emerald",
            path = "/daily-motivation"
        )
    )

    val suggestions = remember { mutableStateListOf<SuggestionItem>().apply { addAll(initial) } }

    val colorMap = mapOf(
        "blue" to Pair(Color(0xFFDBEAFE), Color(0xFF2563EB)),
        "purple" to Pair(Color(0xFFF3E8FF), Color(0xFF7C3AED)),
        "amber" to Pair(Color(0xFFFFF7ED), Color(0xFFF59E0B)),
        "emerald" to Pair(Color(0xFFE6FFFA), Color(0xFF10B981))
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FB)) {
        Column {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6366F1), Color(0xFF7C3AED))
                        )
                    )
                    .padding(start = 16.dp, top = 48.dp, bottom = 16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Stress Relief",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "AI-detected stress patterns",
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            // Intro card
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFF1F5FF), Color(0xFFF5F3FF))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Stress Detected", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Based on your patterns, you may be experiencing stress right now. Try these techniques:",
                                color = Color(0xFF6B7280),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Suggested Activities", fontSize = 16.sp, color = Color(0xFF111827))
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    itemsIndexed(suggestions) { _, item ->
                        val (bg, fg) = colorMap[item.colorKey] ?: Pair(Color(0xFFF3F4F6), Color.Gray)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .clickable {
                                    item.path?.let { raw ->
                                        navController.navigate(raw.removePrefix("/"))
                                    }
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = fg,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, color = Color(0xFF111827))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(item.description, color = Color(0xFF6B7280), fontSize = 14.sp)
                            }

                            Text(item.duration, color = Color(0xFF9CA3AF), fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("ai_chat") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Talk to AI Support", color = Color.White)
                }
            }
        }
    }
}
