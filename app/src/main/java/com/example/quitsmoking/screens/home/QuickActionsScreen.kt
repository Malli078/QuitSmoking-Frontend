package com.example.quitsmoking.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickActionsScreen(navController: NavController) {
    val actions = remember {
        listOf(
            QuickAction(Icons.Default.Flag, "Log a Craving", "craving_alert", Color(0xFFFFE5E5), Color(0xFFEB4D4D)),
            QuickAction(Icons.Default.AirplanemodeActive, "Breathing Exercise", "breathing_exercise", Color(0xFFE1F0FF), Color(0xFF3A7BDC)),
            QuickAction(Icons.AutoMirrored.Filled.Chat, "AI Support Chat", "ai_chat", Color(0xFFF1E5FF), Color(0xFF874BDF)),
            QuickAction(Icons.Default.Favorite, "Health Dashboard", "health_dashboard", Color(0xFFE6F6EA), Color(0xFF2F9E44)),
            QuickAction(Icons.AutoMirrored.Filled.TrendingUp, "View Progress", "achievements", Color(0xFFFFF4D6), Color(0xFFDAA520)),
            QuickAction(Icons.Default.CalendarToday, "Streak Calendar", "streak_calendar", Color(0xFFFFE1F2), Color(0xFFD63384)),
            QuickAction(Icons.Default.AttachMoney, "Money Saved", "money_saved", Color(0xFFE0F7F0), Color(0xFF0F9D58)),
            QuickAction(Icons.Default.AutoAwesome, "Daily Motivation", "daily_motivation", Color(0xFFE6E9FF), Color(0xFF3F51B5)),
            QuickAction(Icons.Default.History, "Craving History", "craving_history", Color(0xFFFFF1E0), Color(0xFFE67622))
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9FAFB))) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF0D9488))))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .padding(top = 28.dp)
        ) {
            Column {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Quick Actions", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                Text("Fast access to all features", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyMedium)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(actions) { item ->
                Surface(shape = RoundedCornerShape(24.dp), modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(item.path) }) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(item.bgColor), contentAlignment = Alignment.Center) {
                            Icon(item.icon, contentDescription = item.label, tint = item.iconColor, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(item.label, color = Color(0xFF111827), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

data class QuickAction(val icon: ImageVector, val label: String, val path: String, val bgColor: Color, val iconColor: Color)
