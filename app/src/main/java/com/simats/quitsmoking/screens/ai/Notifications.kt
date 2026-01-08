package com.simats.quitsmoking.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class UiNotification(
    val icon: ImageVector,
    val title: String,
    val message: String,
    val time: String,
    val colorKey: String,
    val unread: Boolean
)

@Composable
fun NotificationsScreen(navController: NavController) {

    var notifications by remember {
        mutableStateOf(
            listOf(
                UiNotification(
                    icon = Icons.Filled.Favorite,
                    title = "Daily Motivation",
                    message = "Your daily inspiration is ready!",
                    time = "10 min ago",
                    colorKey = "pink",
                    unread = true
                ),
                UiNotification(
                    icon = Icons.Filled.Warning,
                    title = "Craving Alert",
                    message = "High-risk time detected in 30 minutes",
                    time = "1 hour ago",
                    colorKey = "orange",
                    unread = true
                ),
                UiNotification(
                    icon = Icons.Filled.TrendingUp,
                    title = "Milestone Reached!",
                    message = "🎉 7 days smoke-free!",
                    time = "2 hours ago",
                    colorKey = "emerald",
                    unread = false
                ),
                UiNotification(
                    icon = Icons.Filled.AutoAwesome,
                    title = "Health Update",
                    message = "Your lung function has improved by 15%",
                    time = "5 hours ago",
                    unread = false,
                    colorKey = "blue"
                ),
                UiNotification(
                    icon = Icons.Filled.Notifications,
                    title = "Daily Check-in",
                    message = "Time to log your evening progress",
                    time = "Yesterday",
                    unread = false,
                    colorKey = "purple"
                )
            )
        )
    }

    val colorMap = mapOf(
        "pink" to Pair(Color(0xFFFCE7F3), Color(0xFFDB2777)),
        "orange" to Pair(Color(0xFFFFEDD5), Color(0xFFFB923C)),
        "emerald" to Pair(Color(0xFFE6FFFA), Color(0xFF10B981)),
        "blue" to Pair(Color(0xFFDBEAFE), Color(0xFF3B82F6)),
        "purple" to Pair(Color(0xFFF3E8FF), Color(0xFF7C3AED))
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FB)) {
        Column {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF3B82F6), Color(0xFF4F46E5))
                        )
                    )
                    .padding(start = 16.dp, top = 48.dp, bottom = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        "Notifications",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )

                    Button(onClick = {
                        notifications = notifications.map { it.copy(unread = false) }
                    }) {
                        Text("Mark all read")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (notifications.isEmpty()) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.Notifications,
                        null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("No new notifications", color = Color(0xFF6B7280))
                }
                return@Surface
            }

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                itemsIndexed(notifications) { index, item ->
                    val (bg, fg) = colorMap[item.colorKey] ?: Pair(Color(0xFFF3F4F6), Color.Gray)

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable {
                                notifications = notifications.toMutableList().also { list ->
                                    val itn = list[index]
                                    list[index] = itn.copy(unread = !itn.unread)
                                }
                            }
                            .padding(12.dp)
                    ) {

                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(bg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    item.icon,
                                    null,
                                    tint = fg,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item.title, color = Color(0xFF111827))
                                    Spacer(Modifier.weight(1f))
                                    if (item.unread) {
                                        Box(
                                            Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF2563EB))
                                        )
                                    }
                                }

                                Spacer(Modifier.height(4.dp))
                                Text(item.message, color = Color(0xFF6B7280), fontSize = 14.sp)
                                Spacer(Modifier.height(6.dp))
                                Text(item.time, color = Color(0xFF9CA3AF), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
