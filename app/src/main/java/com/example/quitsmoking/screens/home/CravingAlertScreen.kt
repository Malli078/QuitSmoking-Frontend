// CravingAlertScreen.kt
package com.example.quitsmoking.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * CravingAlertScreen
 *
 * Keeps the same UI you provided but avoids creating a real NavController in preview,
 * which caused `Null cannot be a value of a non-null type 'android.content.Context'`.
 *
 * Implementation:
 * - CravingAlertScreen(navController) -> calls CravingAlertContent with lambdas that use navController
 * - CravingAlertContent(...) -> the actual UI, accepts navigation lambdas so Preview can pass no-op lambdas
 */

/* -------------------- Public entry used by app -------------------- */
@Composable
fun CravingAlertScreen(navController: NavController) {
    CravingAlertContent(
        onNavigateHome = { navController.navigate("home") { popUpTo("home") } },
        onLogCraving = { navController.navigate("craving_severity") },
        onBreathing = { navController.navigate("breathing_exercise") },
        onAiChat = { navController.navigate("ai_chat") },
        onTimer = { navController.navigate("craving_timer") }
    )
}

/* -------------------- Core UI (reusable for preview) -------------------- */
@Composable
fun CravingAlertContent(
    onNavigateHome: () -> Unit,
    onLogCraving: () -> Unit,
    onBreathing: () -> Unit,
    onAiChat: () -> Unit,
    onTimer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFEF4444), Color(0xFFF97316))
                )
            )
    ) {

        /* -------------------- FIXED CLOSE BUTTON (TOP RIGHT) -------------------- */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, end = 16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                onClick = onNavigateHome,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White.copy(alpha = 0.18f))
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        /* -------------------- MAIN UI CONTENT -------------------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Icon circle
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(48.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Alert",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Having a Craving?",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Don't worry, we're here to help you get through this.",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                FilledActionButton(
                    title = "Log This Craving",
                    subtitle = "Track and overcome it",
                    onClick = onLogCraving,
                    icon = {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = Color(0xFFDC2626)
                        )
                    }
                )

                OutlineActionButton(
                    title = "Breathing Exercise",
                    subtitle = "Calm down instantly",
                    onClick = onBreathing,
                    icon = {
                        Icon(
                            Icons.Filled.Air,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                OutlineActionButton(
                    title = "Talk to AI Support",
                    subtitle = "Get instant guidance",
                    onClick = onAiChat,
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Message,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                OutlineActionButton(
                    title = "5-Minute Timer",
                    subtitle = "Wait it out",
                    onClick = onTimer,
                    icon = {
                        Icon(
                            Icons.Filled.Timer,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⏱️ Remember: Most cravings pass in 3-5 minutes!",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/* --------------------- BUTTON COMPONENTS --------------------- */

@Composable
private fun FilledActionButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) { icon() }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, color = Color(0xFFDC2626), style = MaterialTheme.typography.bodyLarge)
                    Text(subtitle, color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("→", color = Color(0xFF111827), style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun OutlineActionButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) { icon() }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    Text(subtitle, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("→", color = Color.White, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

/* -------------------- PREVIEW -------------------- */

@Preview(showBackground = true)
@Composable
fun CravingAlertPreview() {
    // Use CravingAlertContent with no-op navigation lambdas so preview doesn't need a NavController/context
    CravingAlertContent(
        onNavigateHome = {},
        onLogCraving = {},
        onBreathing = {},
        onAiChat = {},
        onTimer = {}
    )
}
