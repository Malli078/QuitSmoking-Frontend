package com.simats.quitsmoking.screens.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simats.quitsmoking.model.NotificationSettings
import com.simats.quitsmoking.viewmodel.NotificationSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    viewModel: NotificationSettingsViewModel = viewModel()
) {
    val context = LocalContext.current

    val userPrefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    val userId = userPrefs.getInt("user_id", 0)

    val settings by viewModel.settings.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var pushEnabled by remember { mutableStateOf(true) }
    var milestoneEnabled by remember { mutableStateOf(true) }
    var dailyReminderEnabled by remember { mutableStateOf(true) }
    var emailEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrateEnabled by remember { mutableStateOf(true) }
    var dndStart by remember { mutableStateOf("22:00") }
    var dndEnd by remember { mutableStateOf("07:00") }

    LaunchedEffect(Unit) {
        viewModel.loadSettings(userId)
    }

    LaunchedEffect(settings) {
        settings?.let {
            pushEnabled = it.push_enabled == 1
            milestoneEnabled = it.milestone_enabled == 1
            dailyReminderEnabled = it.daily_reminder_enabled == 1
            emailEnabled = it.email_enabled == 1
            soundEnabled = it.sound_enabled == 1
            vibrateEnabled = it.vibrate_enabled == 1
            dndStart = it.dnd_start
            dndEnd = it.dnd_end
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {
        /* ---------- Header ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF10B981), Color(0xFF059669))
                    )
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 40.dp, start = 8.dp)
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
                    text = "Notification Settings",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 22.sp
                )
                Text(
                    text = "Customize how you stay updated",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        /* ---------- Main Content ---------- */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {

            if (error != null) {
                Text(error!!, color = Color.Red)
                Spacer(Modifier.height(12.dp))
            }

            SettingCard("Push Notifications", "Receive push notifications on this device", pushEnabled) {
                pushEnabled = it
            }

            SettingCard("Milestone Notifications", "Celebrate milestones (1 day, 1 week, etc.)", milestoneEnabled) {
                milestoneEnabled = it
            }

            SettingCard("Daily Reminder", "A daily check-in / motivation notification", dailyReminderEnabled) {
                dailyReminderEnabled = it
            }

            SettingCard("Email Notifications", "Receive occasional emails", emailEnabled) {
                emailEnabled = it
            }

            Spacer(Modifier.height(16.dp))
            Text("Sound & Vibration", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Spacer(Modifier.height(8.dp))

            SettingCard("Sound", "Play a sound when receiving notifications", soundEnabled) {
                soundEnabled = it
            }

            SettingCard("Vibrate", "Vibrate on notification", vibrateEnabled) {
                vibrateEnabled = it
            }

            Spacer(Modifier.height(20.dp))
            Text("Do Not Disturb", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Text(
                "Notifications will be silenced between the times below.",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = dndStart,
                    onValueChange = { dndStart = it },
                    label = { Text("From") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = dndEnd,
                    onValueChange = { dndEnd = it },
                    label = { Text("To") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.saveSettings(
                        userId,
                        NotificationSettings(
                            push_enabled = if (pushEnabled) 1 else 0,
                            milestone_enabled = if (milestoneEnabled) 1 else 0,
                            daily_reminder_enabled = if (dailyReminderEnabled) 1 else 0,
                            email_enabled = if (emailEnabled) 1 else 0,
                            sound_enabled = if (soundEnabled) 1 else 0,
                            vibrate_enabled = if (vibrateEnabled) 1 else 0,
                            dnd_start = dndStart,
                            dnd_end = dndEnd
                        )
                    ) {
                        navController.popBackStack()
                    }
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF10B981))
            ) {
                if (loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Changes", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

/* ---------- Reusable Setting Card ---------- */
@Composable
private fun SettingCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF10B981),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }
    }
}
