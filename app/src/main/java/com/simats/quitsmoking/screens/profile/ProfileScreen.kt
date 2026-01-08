@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.simats.quitsmoking.screens.profile

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

/* ---------- Bitmap Helpers ---------- */
private fun bitmapFromUri(context: Context, uri: Uri): Bitmap? =
    try {
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }
    } catch (e: Exception) {
        null
    }

private fun bitmapToBase64(bitmap: Bitmap): String {
    val output = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
    return Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
}

private fun base64ToBitmap(encoded: String): Bitmap? =
    try {
        val bytes = Base64.decode(encoded, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
        null
    }

/* ---------- Profile Screen ---------- */
@Composable
fun ProfileScreen(
    navController: NavController,
    appVersion: String = "QuitSmart v1.0.0"
) {
    val context = LocalContext.current
    val userPrefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    val habitPrefs = context.getSharedPreferences("user_habits", Context.MODE_PRIVATE)

    var userName by remember { mutableStateOf("User") }
    var userEmail by remember { mutableStateOf("user@example.com") }
    var quitDateString by remember { mutableStateOf<String?>(null) }

    var cigarettesPerDay by remember { mutableStateOf(0) }
    var cigarettesPerPack by remember { mutableStateOf(20) }
    var costPerPack by remember { mutableStateOf(0) }

    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showChooser by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    /* ---------- Load Stored Data ---------- */
    LaunchedEffect(Unit) {
        userName = userPrefs.getString("name", "User") ?: "User"
        userEmail = userPrefs.getString("email", "user@example.com") ?: "user@example.com"
        quitDateString = userPrefs.getString("quit_date", null)

        cigarettesPerDay = habitPrefs.getInt("cigarettes_per_day", 0)
        cigarettesPerPack = habitPrefs.getInt("cigarettes_per_pack", 20)
        costPerPack = habitPrefs.getInt("cost_per_pack", 0)

        userPrefs.getString("avatar_base64", null)?.let {
            avatarBitmap = base64ToBitmap(it)
        }
    }

    /* ---------- Calculations ---------- */
    val daysQuit = remember(quitDateString) {
        if (quitDateString == null) 0 else try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val quitDate = sdf.parse(quitDateString!!)
            val diff = Date().time - (quitDate?.time ?: Date().time)
            maxOf(0, (diff / (1000 * 60 * 60 * 24)).toInt())
        } catch (_: Exception) {
            0
        }
    }

    val milestoneDays = listOf(1, 3, 7, 14, 30, 90, 365)
    val badges = milestoneDays.count { daysQuit >= it }

    val dailySpend =
        if (cigarettesPerPack > 0)
            (cigarettesPerDay.toDouble() / cigarettesPerPack) * costPerPack
        else 0.0

    val moneySaved = (dailySpend * daysQuit).toInt()

    /* ---------- Image Pickers ---------- */
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                bitmapFromUri(context, it)?.let { bmp ->
                    avatarBitmap = bmp
                    userPrefs.edit()
                        .putString("avatar_base64", bitmapToBase64(bmp))
                        .apply()
                }
            }
            showChooser = false
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
            bmp?.let {
                avatarBitmap = it
                userPrefs.edit()
                    .putString("avatar_base64", bitmapToBase64(it))
                    .apply()
            }
            showChooser = false
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) cameraLauncher.launch(null)
            else Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }

    val menuItems = listOf(
        MenuItem(Icons.Default.Person, "Edit Personal Information", "edit_profile"),
        MenuItem(Icons.Default.CalendarMonth, "Habit Settings", "habit_settings"),
        MenuItem(Icons.Default.Settings, "Quit Plan Settings", "quit_plan_settings"),
        MenuItem(Icons.Default.Notifications, "Notification Settings", "notification_settings"),
        MenuItem(Icons.Default.Settings, "App Settings", "app_settings"),
        MenuItem(Icons.Default.Security, "Privacy & Security", "privacy"),
        MenuItem(Icons.AutoMirrored.Filled.Help, "Help & Support", "help_support")
    )

    /* ---------------- UI ---------------- */
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6F8))
    ) {
        /* ---------- Header ---------- */
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF10B981), Color(0xFF059669))
                        )
                    )
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 36.dp) // ⬅️ slightly more bottom space
            ) {
                Column {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(70.dp)) {
                            if (avatarBitmap != null) {
                                Image(
                                    bitmap = avatarBitmap!!.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, null, tint = Color(0xFF059669))
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF047857))
                                    .align(Alignment.BottomEnd)
                                    .clickable { showChooser = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.White, fontSize = 14.sp)
                            }
                        }

                        Spacer(Modifier.width(14.dp))

                        Column {
                            Text(
                                userName,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                userEmail,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        /* ---------- Stats Card (Moved Lower) ---------- */
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-12).dp), // ⬇️ lowered from -26dp → -12dp
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(daysQuit.toString(), "Days Quit")
                    StatItem(badges.toString(), "Badges")
                    StatItem("₹$moneySaved", "Saved")
                }
            }
        }

        /* ---------- Menu Items ---------- */
        items(menuItems) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { navController.navigate(item.route) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(item.icon, null, tint = Color.Black)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = item.label,
                        modifier = Modifier.weight(1f),
                        color = Color.Black
                    )
                    Icon(Icons.Default.ChevronRight, null, tint = Color.Black)
                }
            }
        }

        /* ---------- Logout ---------- */
        item {
            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { showLogoutDialog = true },
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = Color.Red)
                    Spacer(Modifier.width(12.dp))
                    Text("Log Out", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                appVersion,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(Modifier.height(24.dp))
        }
    }

    /* ---------- Dialogs ---------- */
    if (showChooser) {
        AlertDialog(
            onDismissRequest = { showChooser = false },
            title = { Text("Set profile photo") },
            confirmButton = {
                TextButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Camera")
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    userPrefs.edit().clear().apply()
                    navController.navigate("login") {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }) {
                    Text("Log Out", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/* ---------- Stat Item ---------- */
@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 22.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(label, fontSize = 13.sp, color = Color.Gray)
    }
}

private data class MenuItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val route: String
)
