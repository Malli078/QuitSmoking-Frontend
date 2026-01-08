package com.simats.quitsmoking.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class TipItem(
    val title: String,
    val tip: String,
    val category: String
)

@Composable
fun PersonalizedTipsScreen(navController: NavController) {
    var savedTips by remember { mutableStateOf(listOf<Int>()) }

    val tips = listOf(
        TipItem(
            title = "Morning Routine",
            tip = "Replace your morning cigarette with a glass of water and deep breathing. This helps reset the habit loop.",
            category = "Habit Breaking"
        ),
        TipItem(
            title = "Trigger Management",
            tip = "When you feel the urge after coffee, try chewing gum or taking a 2-minute walk instead.",
            category = "Coping Strategy"
        ),
        TipItem(
            title = "Social Situations",
            tip = "Prepare your response: \"I'm on a journey to better health.\" Practice saying it with confidence.",
            category = "Social Support"
        ),
        TipItem(
            title = "Stress Relief",
            tip = "Keep a stress ball or fidget toy handy. Physical activity helps redirect the urge to smoke.",
            category = "Stress Management"
        ),
        TipItem(
            title = "Sleep Better",
            tip = "Your sleep quality is improving! Stick to a bedtime routine to support your recovery.",
            category = "Health Recovery"
        )
    )

    fun toggleSave(index: Int) {
        savedTips = if (savedTips.contains(index)) {
            savedTips.filter { it != index }
        } else {
            savedTips + index
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FB)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF10B981), Color(0xFF0F766E))
                        )
                    )
                    .padding(start = 16.dp, top = 48.dp, bottom = 20.dp)
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
                            text = "Personalized Tips",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "AI-curated advice for you",
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
                                colors = listOf(Color(0xFFECFDF5), Color(0xFFF0FEFC))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Tailored for You", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "These tips are based on your smoking habits, triggers, and progress.",
                                color = Color(0xFF6B7280),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tips list
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    tips.forEachIndexed { index, t ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = null,
                                        tint = Color(0xFFEC4899),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = t.category, color = Color(0xFF6B7280), fontSize = 12.sp)
                                }

                                IconButton(onClick = { toggleSave(index) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Bookmark,
                                        contentDescription = "Save",
                                        tint = if (savedTips.contains(index)) Color(0xFF059669) else Color(0xFF9CA3AF)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = t.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = t.tip, color = Color(0xFF6B7280), fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Saved summary card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (savedTips.isNotEmpty()) {
                                "You've saved ${savedTips.size} tip${if (savedTips.size > 1) "s" else ""}"
                            } else {
                                "Tap the bookmark to save tips"
                            },
                            color = Color(0xFF6B7280),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
