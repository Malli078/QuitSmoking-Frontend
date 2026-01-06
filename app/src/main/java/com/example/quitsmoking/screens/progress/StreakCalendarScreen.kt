package com.example.quitsmoking.screens.progress

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quitsmoking.viewmodel.StreakViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StreakCalendarScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val todayCal = Calendar.getInstance()

    var year by remember { mutableStateOf(todayCal.get(Calendar.YEAR)) }
    var month by remember { mutableStateOf(todayCal.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }

    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val monthLabel = SimpleDateFormat(
        "MMMM yyyy",
        Locale.getDefault()
    ).format(
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
        }.time
    )

    // 🔗 ViewModel
    val viewModel: StreakViewModel = viewModel()
    val calendarData by viewModel.calendar.collectAsState()
    val stats by viewModel.stats.collectAsState()

    val userId = 1 // TODO: replace with logged-in user id

    // 🔥 Load backend data
    LaunchedEffect(year, month) {
        viewModel.loadMonth(
            userId = userId,
            year = year,
            month = month + 1
        )
        viewModel.loadStats(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF8FAFC))
    ) {

        /* ---------- HEADER ---------- */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFF7A18), Color(0xFFFF3D3D))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigate("home") }
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Streak Calendar",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your smoke-free journey",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ---------- CURRENT STREAK ---------- */

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Current Streak", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        "${stats?.currentStreak ?: 0} days",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFFF7A18), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ---------- QUICK ACTIONS ---------- */

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                icon = Icons.Default.MilitaryTech,
                label = "Achievements",
                modifier = Modifier.weight(1f)
            ) { navController.navigate("achievements") }

            QuickActionCard(
                icon = Icons.Default.AttachMoney,
                label = "Money Saved",
                modifier = Modifier.weight(1f)
            ) { navController.navigate("money_saved") }

            QuickActionCard(
                icon = Icons.Default.EmojiEvents,
                label = "Milestones",
                modifier = Modifier.weight(1f)
            ) { navController.navigate("milestone") }
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- MONTH NAV ---------- */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "‹",
                fontSize = 22.sp,
                modifier = Modifier.clickable {
                    if (month == 0) {
                        month = 11
                        year--
                    } else month--
                    selectedDay = null
                }
            )
            Text(monthLabel, fontWeight = FontWeight.Bold)
            Text(
                "›",
                fontSize = 22.sp,
                modifier = Modifier.clickable {
                    if (month == 11) {
                        month = 0
                        year++
                    } else month++
                    selectedDay = null
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        /* ---------- DAYS HEADER ---------- */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            daysOfWeek.forEach {
                Text(
                    it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        /* ---------- CALENDAR GRID ---------- */

        val tempCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOffset = tempCal.get(Calendar.DAY_OF_WEEK) - 1

        val cells = mutableListOf<Int?>()
        repeat(firstDayOffset) { cells.add(null) }
        for (d in 1..daysInMonth) cells.add(d)

        cells.chunked(7).forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                week.forEach { day ->
                    val isSelected = day == selectedDay

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(
                                if (isSelected) Color(0xFFFF7A18) else Color.White,
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = if (day == todayCal.get(Calendar.DAY_OF_MONTH)
                                    && month == todayCal.get(Calendar.MONTH)
                                    && year == todayCal.get(Calendar.YEAR)
                                ) 2.dp else 0.dp,
                                color = Color(0xFFFF7A18),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable(enabled = day != null) {
                                selectedDay = day
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (day != null) {
                            Text(
                                text = day.toString(),
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF111827)
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
        }

        Spacer(Modifier.height(80.dp))
    }
}

/* ===================================================== */
/* ================= QUICK ACTION CARD ================= */
/* ===================================================== */

@Composable
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val elevation by animateDpAsState(
        targetValue = if (pressed) 2.dp else 8.dp,
        animationSpec = tween(150),
        label = "elevation"
    )

    Card(
        modifier = modifier
            .height(96.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    }
                )
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFFFF7A18).copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color(0xFFFF7A18),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827)
            )
        }
    }
}
