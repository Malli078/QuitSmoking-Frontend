package com.simats.quitsmoking.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset

@Composable
fun NotificationScreen(navController: NavController) {

    var settings by remember {
        mutableStateOf(
            mutableMapOf(
                "cravingPredictions" to true,
                "dailyMotivation" to true,
                "milestones" to true,
                "healthUpdates" to true,
                "triggerAlerts" to true,
                "checkIns" to false,
                "tips" to true,
                "emergencySupport" to true
            )
        )
    }

    val settingsList = listOf(
        Triple("cravingPredictions", "Craving Predictions", "AI-powered craving alerts"),
        Triple("dailyMotivation", "Daily Motivation", "Morning inspiration quotes"),
        Triple("milestones", "Milestone Celebrations", "Achievement notifications"),
        Triple("healthUpdates", "Health Updates", "Recovery progress alerts"),
        Triple("triggerAlerts", "Trigger Detection", "High-risk situation warnings"),
        Triple("checkIns", "Daily Check-ins", "Reminder to log your day"),
        Triple("tips", "Personalized Tips", "Custom quit advice"),
        Triple("emergencySupport", "Emergency Support", "Critical craving alerts")
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FB)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF3B82F6), Color(0xFF4F46E5))
                        )
                    )
                    .padding(start = 20.dp, top = 48.dp, bottom = 28.dp)
            ) {
                Column {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "AI Notification Settings",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Manage AI-specific alerts",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                }
            }

            // Settings list
            Column(modifier = Modifier.padding(20.dp)) {
                settingsList.forEach { (key, label, desc) ->
                    val isEnabled = settings[key] == true

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White)
                            .clickable {
                                settings = settings.toMutableMap().also { map ->
                                    map[key] = !(map[key] ?: false)
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon circle
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isEnabled) Color(0xFFDCFCE7) else Color(0xFFF3F4F6)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isEnabled) Icons.Filled.Notifications else Icons.Filled.NotificationsOff,
                                contentDescription = null,
                                tint = if (isEnabled) Color(0xFF059669) else Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Label + description
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = label, color = Color(0xFF111827), style = MaterialTheme.typography.bodyLarge)
                            Text(text = desc, color = Color(0xFF6B7280), fontSize = 12.sp)
                        }

                        // Simple switch visual (no dependency on resources)
                        Box(
                            modifier = Modifier
                                .width(46.dp)
                                .height(26.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (isEnabled) Color(0xFF059669) else Color(0xFFD1D5DB)),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .offset(x = if (isEnabled) 20.dp else 2.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        }
                    }
                }
            }
        }
    }
}
