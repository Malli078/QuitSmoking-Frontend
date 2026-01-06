@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.quitsmoking.screens.profile

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private const val TAG = "APP_SETTINGS_SAFE"

/* ---------------- DARK MODE HELPER ---------------- */
private fun applyDarkMode(enabled: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (enabled)
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO
    )
}

/* ---------------- SCREEN ---------------- */
@Composable
fun AppSettingsScreen(navController: NavController) {

    val context = LocalContext.current

    val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    var darkMode by remember { mutableStateOf(prefs.getBoolean("dark_mode", false)) }

    var notifications by remember { mutableStateOf(true) }
    var soundEffects by remember { mutableStateOf(true) }
    var hapticFeedback by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "App Settings",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { safePop(navController) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Button(
                    onClick = {
                        applyDarkMode(darkMode)
                        prefs.edit()
                            .putBoolean("dark_mode", darkMode)
                            .apply()
                        safePop(navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Changes", color = Color.White)
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                "Customize your experience",
                color = Color.Black,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(16.dp))

            SectionHeader("Appearance")

            SettingsToggleItem(
                title = "Dark Mode",
                subtitle = "Easier on the eyes",
                icon = Icons.Default.DarkMode,
                bgColor = Color(0xFFE5E7EB),
                tint = Color.Black,
                isOn = darkMode,
                onToggle = { darkMode = !darkMode }
            )

            Spacer(Modifier.height(20.dp))
            SectionHeader("Notifications")

            SettingsToggleItem(
                title = "Push Notifications",
                subtitle = "Stay updated",
                icon = Icons.Default.Notifications,
                bgColor = Color(0xFFE5E7EB),
                tint = Color.Black,
                isOn = notifications,
                onToggle = { notifications = !notifications }
            )

            SettingsNavigationItem(
                title = "Notification Preferences",
                onClick = { safeNavigate(navController, "notification_settings") }
            )

            Spacer(Modifier.height(20.dp))
            SectionHeader("Preferences")

            SettingsToggleItem(
                title = "Sound Effects",
                subtitle = "Audio feedback",
                icon = Icons.Default.Smartphone,
                bgColor = Color(0xFFE5E7EB),
                tint = Color.Black,
                isOn = soundEffects,
                onToggle = { soundEffects = !soundEffects }
            )

            SettingsToggleItem(
                title = "Haptic Feedback",
                subtitle = "Vibration feedback",
                icon = Icons.Default.Smartphone,
                bgColor = Color(0xFFE5E7EB),
                tint = Color.Black,
                isOn = hapticFeedback,
                onToggle = { hapticFeedback = !hapticFeedback }
            )

            Spacer(Modifier.height(20.dp))
            SectionHeader("Other")

            SettingsNavigationItem(
                title = "Privacy & Security",
                subtitle = "Your data protection",
                icon = Icons.Default.Lock,
                bgColor = Color(0xFFE5E7EB),
                tint = Color.Black,
                onClick = { safeNavigate(navController, "privacy") }
            )

            SettingsNavigationItem(
                title = "Language",
                subtitle = "English",
                icon = Icons.Default.Language,
                bgColor = Color(0xFFE5E7EB),
                tint = Color.Black,
                onClick = { }
            )

            Spacer(Modifier.height(80.dp))
        }
    }
}

/* ---------------- UI HELPERS ---------------- */

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun IconCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bg: Color,
    tint: Color
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint)
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    tint: Color,
    isOn: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconCircle(icon, bgColor, tint)
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onToggle() }
            ) {
                Text(title, fontSize = 16.sp, color = Color.Black)
                Text(subtitle, fontSize = 13.sp, color = Color.Black)
            }
            Switch(checked = isOn, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
private fun SettingsNavigationItem(
    title: String,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    bgColor: Color = Color.LightGray,
    tint: Color = Color.Black,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                IconCircle(icon, bgColor, tint)
                Spacer(Modifier.width(16.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, color = Color.Black)
                subtitle?.let {
                    Text(it, fontSize = 13.sp, color = Color.Black)
                }
            }
            Text("→", color = Color.Black, fontSize = 20.sp)
        }
    }
}

/* ---------------- SAFE NAV HELPERS ---------------- */

private fun safeNavigate(navController: NavController, route: String) {
    try {
        navController.navigate(route)
    } catch (e: Exception) {
        Log.e(TAG, "navigate failed: $route", e)
    }
}

private fun safePop(navController: NavController) {
    try {
        navController.popBackStack()
    } catch (e: Exception) {
        Log.e(TAG, "pop failed", e)
    }
}
