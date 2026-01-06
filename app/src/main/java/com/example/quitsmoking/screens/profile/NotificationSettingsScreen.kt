package com.example.quitsmoking.screens.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.quitsmoking.model.NotificationSettings
import com.example.quitsmoking.viewmodel.NotificationSettingsViewModel

@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    viewModel: NotificationSettingsViewModel = viewModel()
) {
    val context = LocalContext.current

    // 🔐 USER ID
    val userPrefs =
        context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    val userId = userPrefs.getInt("user_id", 0)

    val settings by viewModel.settings.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // UI STATE
    var pushEnabled by remember { mutableStateOf(true) }
    var milestoneEnabled by remember { mutableStateOf(true) }
    var dailyReminderEnabled by remember { mutableStateOf(true) }
    var emailEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrateEnabled by remember { mutableStateOf(true) }
    var dndStart by remember { mutableStateOf("22:00") }
    var dndEnd by remember { mutableStateOf("07:00") }

    // 🔄 LOAD FROM BACKEND
    LaunchedEffect(Unit) {
        viewModel.loadSettings(userId)
    }

    // 🔁 UPDATE UI WHEN DATA ARRIVES
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

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 10.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Notification Settings",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        bottomBar = {
            Surface(tonalElevation = 6.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(12.dp)
                ) {
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
                            .height(52.dp)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Save Changes")
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tip: You can also control notifications in system settings.",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            if (error != null) {
                Text(error!!, color = Color.Red)
                Spacer(Modifier.height(12.dp))
            }

            SettingRow(
                "Push Notifications",
                "Receive push notifications on this device",
                pushEnabled
            ) { pushEnabled = it }

            Spacer(Modifier.height(12.dp))

            SettingRow(
                "Milestone Notifications",
                "Celebrate milestones (1 day, 1 week, etc.)",
                milestoneEnabled
            ) { milestoneEnabled = it }

            Spacer(Modifier.height(12.dp))

            SettingRow(
                "Daily Reminder",
                "A daily check-in / motivation notification",
                dailyReminderEnabled
            ) { dailyReminderEnabled = it }

            Spacer(Modifier.height(12.dp))

            SettingRow(
                "Email Notifications",
                "Receive occasional emails",
                emailEnabled
            ) { emailEnabled = it }

            Spacer(Modifier.height(20.dp))

            Text("Sound & Vibration", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            SettingRow(
                "Sound",
                "Play a sound when receiving notifications",
                soundEnabled
            ) { soundEnabled = it }

            Spacer(Modifier.height(8.dp))

            SettingRow(
                "Vibrate",
                "Vibrate on notification",
                vibrateEnabled
            ) { vibrateEnabled = it }

            Spacer(Modifier.height(20.dp))

            Text("Do Not Disturb", style = MaterialTheme.typography.titleMedium)
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
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = dndEnd,
                    onValueChange = { dndEnd = it },
                    label = { Text("To") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(200.dp))
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = Color.Gray, fontSize = 13.sp)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
