@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.simats.quitsmoking.screens.profile

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simats.quitsmoking.viewmodel.PrivacySecurityViewModel

@Composable
fun PrivacySecurityScreen(
    navController: NavController,
    viewModel: PrivacySecurityViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    val userId = prefs.getInt("user_id", 0)

    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Toast messages
    LaunchedEffect(message) {
        message?.let {
            android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
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
                    text = "Privacy & Security",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 22.sp
                )
                Text(
                    text = "Your data protection settings",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        /* ---------- Content ---------- */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text("Your data is protected", color = Color.Black, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 4 }
            ) {
                InfoCard()
            }

            Spacer(Modifier.height(20.dp))

            AnimatedMenuButton(
                title = "Privacy Policy",
                subtitle = "How we protect your data",
                icon = Icons.Default.Policy,
                onClick = {}
            )

            AnimatedMenuButton(
                title = "Data Encryption",
                subtitle = "End-to-end security enabled",
                icon = Icons.Default.Lock,
                trailingText = "Enabled",
                trailingColor = Color(0xFF059669),
                onClick = {}
            )

            AnimatedMenuButton(
                title = "Export My Data",
                subtitle = "Download your information safely",
                icon = Icons.Default.Download,
                onClick = { viewModel.exportData(userId) }
            )

            AnimatedMenuButton(
                title = "Delete Account",
                subtitle = "Permanently remove your data",
                icon = Icons.Default.Delete,
                iconTint = Color.Red,
                onClick = { showDeleteDialog = true }
            )

            Spacer(Modifier.height(32.dp))
        }
    }

    /* ---------- Delete Account Dialog ---------- */
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account", color = Color.Black) },
            text = { Text("This action is permanent. Are you sure?", color = Color.Black) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAccount(userId) {
                        prefs.edit().clear().apply()
                        navController.navigate("login") {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color.Black)
                }
            }
        )
    }

    /* ---------- Loading Indicator ---------- */
    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF059669))
        }
    }
}

/* ---------- Info Card ---------- */
@Composable
private fun InfoCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Security, null, tint = Color.Black)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Your Privacy Matters", fontSize = 16.sp, color = Color.Black)
                Text(
                    "All your data is encrypted and securely stored.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/* ---------- Animated Menu Button ---------- */
@Composable
private fun AnimatedMenuButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color = Color.Black,
    trailingText: String? = null,
    trailingColor: Color = Color.Black,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .scale(scale)
            .clickable {
                pressed = true
                onClick()
                pressed = false
            }
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint)
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, color = Color.Black)
                Text(subtitle, fontSize = 13.sp, color = Color.Gray)
            }
            trailingText?.let {
                Text(it, color = trailingColor)
            } ?: Text("→", fontSize = 22.sp, color = Color.Black)
        }
    }
}
