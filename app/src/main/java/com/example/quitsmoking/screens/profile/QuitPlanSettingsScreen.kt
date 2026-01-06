package com.example.quitsmoking.screens.profile

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quitsmoking.model.QuitMilestone
import com.example.quitsmoking.viewmodel.QuitPlanViewModel
import java.text.SimpleDateFormat
import java.util.*

data class Milestone(
    val days: Int,
    val label: String,
    val enabled: Boolean
)

@Composable
fun QuitPlanSettingsScreen(
    navController: NavController,
    viewModel: QuitPlanViewModel = viewModel()
) {
    val context = LocalContext.current

    val prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    val userId = prefs.getInt("user_id", 0)

    val quitDateFromBackend by viewModel.quitDate.collectAsState()
    val backendMilestones by viewModel.milestones.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedQuitDate by remember { mutableStateOf<String?>(null) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val picked = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedQuitDate = sdf.format(picked.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val milestoneLabels = mapOf(
        1 to "24 Hours",
        3 to "3 Days",
        7 to "1 Week",
        14 to "2 Weeks",
        30 to "1 Month",
        90 to "3 Months",
        365 to "1 Year"
    )

    val milestones = remember { mutableStateListOf<Milestone>() }

    LaunchedEffect(Unit) {
        if (userId > 0) {
            viewModel.loadQuitPlan(userId)
        }
    }

    LaunchedEffect(backendMilestones) {
        milestones.clear()
        backendMilestones.forEach {
            milestones.add(
                Milestone(
                    days = it.days,
                    label = milestoneLabels[it.days] ?: "${it.days} Days",
                    enabled = it.enabled
                )
            )
        }
    }

    LaunchedEffect(quitDateFromBackend) {
        if (!quitDateFromBackend.isNullOrEmpty()) {
            selectedQuitDate = quitDateFromBackend
        }
    }

    fun toggleMilestone(index: Int) {
        val m = milestones[index]
        milestones[index] = m.copy(enabled = !m.enabled)
    }

    val quitDateDisplay = selectedQuitDate?.let {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(it)
            SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(date!!)
        } catch (_: Exception) {
            "Not set"
        }
    } ?: "Not set"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFB))
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 48.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                "Quit Plan",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Text(
                "Manage your milestones",
                color = Color.Black,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(12.dp))
        }

        // 📅 Quit Date (WHITE CARD)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { datePickerDialog.show() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CalendarToday, null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Quit Date", color = Color.Black)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    quitDateDisplay,
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                "Milestone Notifications",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Text(
                "Choose which milestones to celebrate",
                color = Color.Black,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            milestones.forEachIndexed { index, milestone ->
                MilestoneRow(
                    milestone = milestone,
                    onToggle = { toggleMilestone(index) }
                )
                Spacer(Modifier.height(10.dp))
            }
        }

        error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = Color.Red, modifier = Modifier.padding(16.dp))
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                viewModel.saveQuitPlan(
                    userId = userId,
                    quitDate = selectedQuitDate,
                    milestones = milestones.map {
                        QuitMilestone(it.days, it.enabled)
                    }
                ) {
                    navController.popBackStack()
                }
            },
            enabled = !loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(55.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF059669))
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Save Changes", color = Color.White)
            }
        }
    }
}

@Composable
private fun MilestoneRow(
    milestone: Milestone,
    onToggle: () -> Unit
) {
    val circleOffset by animateDpAsState(
        targetValue = if (milestone.enabled) 24.dp else 2.dp,
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (milestone.enabled) Color(0xFFD1FAE5)
                        else Color(0xFFF3F4F6)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (milestone.enabled) Icons.Filled.CheckCircle else Icons.Filled.Flag,
                    null,
                    tint = if (milestone.enabled) Color(0xFF059669) else Color.Black
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    milestone.label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    "${milestone.days} days smoke-free",
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (milestone.enabled) Color(0xFF059669)
                        else Color(0xFFD1D5DB)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .offset(x = circleOffset, y = 2.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
    }
}
