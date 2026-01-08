package com.simats.quitsmoking.screens.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.max
import kotlin.math.roundToInt

private data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val unlocked: Boolean,
    val dateLabel: String
)

@Composable
fun AchievementsScreen(
    navController: NavController,
    userQuitDateEpochMillis: Long? = null
) {
    val today = LocalDate.now(ZoneId.systemDefault())

    val quitDate: LocalDate = remember(userQuitDateEpochMillis) {
        if (userQuitDateEpochMillis != null) {
            Instant.ofEpochMilli(userQuitDateEpochMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } else today
    }

    val daysSinceQuit by remember(quitDate) {
        derivedStateOf {
            max(0, java.time.temporal.ChronoUnit.DAYS.between(quitDate, today).toInt())
        }
    }

    val achievements = remember(daysSinceQuit) {
        listOf(
            AchievementItem("first-day", "First Day", "Completed your first smoke-free day",
                Icons.Default.Star, Color(0xFFFFD54F), daysSinceQuit >= 1,
                if (daysSinceQuit >= 1) "Today" else "Locked"),
            AchievementItem("three-days", "Three Day Warrior", "Nicotine is leaving your body",
                Icons.Default.FlashOn, Color(0xFF3B82F6), daysSinceQuit >= 3,
                if (daysSinceQuit >= 3) "${daysSinceQuit - 3} days ago" else "Locked"),
            AchievementItem("one-week", "Week Champion", "One full week smoke-free!",
                Icons.Default.MilitaryTech, Color(0xFF8B5CF6), daysSinceQuit >= 7,
                if (daysSinceQuit >= 7) "Unlocked" else "${7 - daysSinceQuit} days to go"),
            AchievementItem("two-weeks", "Fortnight Master", "Your circulation is improving",
                Icons.Default.EmojiEvents, Color(0xFF14B8A6), daysSinceQuit >= 14,
                if (daysSinceQuit >= 14) "Unlocked" else "${14 - daysSinceQuit} days to go"),
            AchievementItem("one-month", "Monthly Milestone", "One month of freedom!",
                Icons.Default.LocalOffer, Color(0xFF10B981), daysSinceQuit >= 30,
                if (daysSinceQuit >= 30) "Unlocked" else "${30 - daysSinceQuit} days to go"),
            AchievementItem("three-months", "Quarter Year Legend", "Lung function increased by 30%",
                Icons.Default.Star, Color(0xFF6366F1), daysSinceQuit >= 90,
                if (daysSinceQuit >= 90) "Unlocked" else "${90 - daysSinceQuit} days to go"),
            AchievementItem("six-months", "Half Year Hero", "Your risk dropped significantly",
                Icons.Default.EmojiEvents, Color(0xFFEC4899), daysSinceQuit >= 180,
                if (daysSinceQuit >= 180) "Unlocked" else "${180 - daysSinceQuit} days to go"),
            AchievementItem("one-year", "Annual Champion", "Heart disease risk cut in half!",
                Icons.Default.Verified, Color(0xFFFFB020), daysSinceQuit >= 365,
                if (daysSinceQuit >= 365) "Unlocked" else "${365 - daysSinceQuit} days to go")
        )
    }

    val unlockedCount = achievements.count { it.unlocked }
    val progress by animateFloatAsState(
        targetValue = unlockedCount.toFloat() / achievements.size,
        label = "progress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {

        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFD54F), Color(0xFFF97316))
                    )
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 42.dp, start = 8.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    "Achievement Badges",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$unlockedCount of ${achievements.size} unlocked",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ---------- PROGRESS CARD ---------- */
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Overall Progress", color = Color.Gray)
                    Text("${(progress * 100).roundToInt()}%", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE5E7EB))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFFD54F), Color(0xFFF97316))
                                )
                            )
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- ACHIEVEMENTS GRID ---------- */
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(achievements) { ach ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (ach.unlocked) Color.White else Color(0xFFF3F4F6)
                    ),
                    elevation = CardDefaults.cardElevation(if (ach.unlocked) 6.dp else 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(if (ach.unlocked) ach.color else Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (ach.unlocked) ach.icon else Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(Modifier.height(10.dp))
                        Text(ach.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
                        Spacer(Modifier.height(6.dp))
                        Text(ach.description, fontSize = 12.sp, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            ach.dateLabel,
                            fontSize = 12.sp,
                            color = if (ach.unlocked) Color(0xFF10B981) else Color.Gray
                        )
                    }
                }
            }

            /* ---------- NEXT ACHIEVEMENT CARD ---------- */
            if (unlockedCount < achievements.size) {
                item(span = { GridItemSpan(2) }) {
                    val next = achievements.firstOrNull { !it.unlocked }
                    next?.let {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF7C3AED), Color(0xFFEC4899))
                                        )
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Text("Next Achievement", color = Color.White, fontSize = 12.sp)
                                    Spacer(Modifier.height(6.dp))
                                    Text(it.title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(4.dp))
                                    Text(it.dateLabel, color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
