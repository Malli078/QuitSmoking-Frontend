package com.simats.quitsmoking.screens.home

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.max
import kotlin.math.roundToInt

/* ---------------------- Models ---------------------- */
data class User(
    val id: String,
    val name: String,
    val quitDateEpochMillis: Long? = null,
    val cigarettesPerDay: Int? = 10,
    val costPerPack: Double? = 10.0
)

data class Craving(
    val id: String,
    val timestampEpochMillis: Long,
    val overcome: Boolean = false
)

/* ---------------------- ViewModel ---------------------- */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableStateFlow(
        User(
            id = "u1",
            name = loadUserName(application),
            quitDateEpochMillis = Instant.now().minus(10, ChronoUnit.DAYS).toEpochMilli(),
            cigarettesPerDay = 10,
            costPerPack = 12.0
        )
    )
    val user: StateFlow<User?> = _user

    private val _streak = MutableStateFlow(10)
    val streak: StateFlow<Int> = _streak
    private val _now = MutableStateFlow(System.currentTimeMillis())
    val now: StateFlow<Long> = _now

    init {
        viewModelScope.launch {
            while (true) {
                delay(60_000)
                _now.value = System.currentTimeMillis()
            }
        }
    }

    private val _cravings = MutableStateFlow(
        listOf(
            Craving("c1", Instant.now().minus(9, ChronoUnit.DAYS).toEpochMilli(), true),
            Craving("c2", Instant.now().minus(6, ChronoUnit.DAYS).toEpochMilli(), false),
            Craving("c3", Instant.now().minus(2, ChronoUnit.DAYS).toEpochMilli(), true)
        )
    )
    val cravings: StateFlow<List<Craving>> = _cravings

    private fun loadUserName(app: Application): String {
        val prefs = app.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getString("user_name", "Friend") ?: "Friend"
    }
}

/* ---------------------- HomeScreen ---------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val user by viewModel.user.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val nowMillis by viewModel.now.collectAsState()

    val daysSinceQuit = remember(user, nowMillis) {
        val quitMillis = user?.quitDateEpochMillis ?: return@remember 0L
        val quitDate = Instant.ofEpochMilli(quitMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val today = Instant.ofEpochMilli(nowMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        max(0L, ChronoUnit.DAYS.between(quitDate, today))
    }

    val cigarettesAvoided = daysSinceQuit * (user?.cigarettesPerDay?.toLong() ?: 10L)
    val moneySaved = remember(cigarettesAvoided, user) {
        val costPerPack = user?.costPerPack ?: 10.0
        max(0, ((cigarettesAvoided / 20.0) * costPerPack).roundToInt())
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            // Custom Top Header (Better alignment + visual polish)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF10B981), Color(0xFF059669))
                        )
                    )
                    .padding(top = 48.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Hello, ${user?.name ?: "Friend"} 👋",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "You're doing amazing!",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { navController.navigate("notifications") }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                        }
                    }
                }
            }
        },
        bottomBar = { HomeBottomNavigation(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            /* ------------------- Main Stats Card ------------------- */
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Smoke-free for", color = Color(0xFFBBF7D0))
                            Text(
                                "$daysSinceQuit days",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF065F46)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFFB923C),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Divider(color = Color(0xFF15803D))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem("Cigarettes avoided", cigarettesAvoided.toInt())
                        StatItem("Money saved", moneySaved)
                        StatItem("Streak", streak)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ---------------- Quick Actions ---------------- */
            Text("Quick Actions", fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(Modifier.height(8.dp))
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionButton(
                        Modifier.weight(1f),
                        "Log Craving",
                        Icons.Default.Place
                    ) { navController.navigate("craving-alert") }
                    QuickActionButton(
                        Modifier.weight(1f),
                        "AI Support",
                        Icons.AutoMirrored.Filled.Message
                    ) { navController.navigate("ai_chat") }
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionButton(
                        Modifier.weight(1f),
                        "Craving Predictions",
                        Icons.AutoMirrored.Filled.TrendingUp
                    ) { navController.navigate("craving_prediction") }
                    QuickActionButton(
                        Modifier.weight(1f),
                        "Daily Progress",
                        Icons.Default.Assessment
                    ) { navController.navigate("daily_progress") }
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ---------------- Daily Motivation ---------------- */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("daily_motivation") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Today's Motivation", color = Color.Black)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "\"Every moment you resist is a victory. Your body is healing right now.\"",
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("Tap for more inspiration →", color = Color.Black, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ---------------- Today's Goals ---------------- */
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Today's Goals", fontWeight = FontWeight.SemiBold, color = Color.Black)
                        TextButton(onClick = { navController.navigate("todays_goals") }) {
                            Text("View All", color = Color.Black)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GoalRow("Morning breathing exercise", true)
                        GoalRow("Log afternoon check-in", false)
                        GoalRow("Evening gratitude journal", false)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ---------------------- Components ---------------------- */
@Composable
private fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = Color(0xFFBBF7D0))
        Text("$value", fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun QuickActionButton(
    modifier: Modifier,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(110.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = Color.Black)
            }
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 13.sp, color = Color.Black)
        }
    }
}

@Composable
private fun GoalRow(text: String, completed: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (completed) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF10B981)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(text, color = Color.Black)
    }
}
