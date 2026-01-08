// CravingAlertScreen.kt
package com.simats.quitsmoking.screens.home

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

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

/* -------------------- CORE CONTENT -------------------- */
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
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEF4444), Color(0xFFF97316))
                )
            )
    ) {
        /* ---------- TOP CLOSE BUTTON ---------- */
        IconButton(
            onClick = onNavigateHome,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 32.dp, end = 20.dp)
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

        /* ---------- CENTERED MAIN CONTENT ---------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
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

            Text(
                text = "Having a Craving?",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Don't worry, we're here to help you get through this.",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            /* ---------- BUTTONS ---------- */
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

            /* ---------- BOTTOM REMINDER ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⏱️ Remember: Most cravings pass in 3–5 minutes!",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/* -------------------- BUTTON COMPONENTS -------------------- */

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
            Icon(Icons.Filled.ArrowForwardIos, contentDescription = null, tint = Color(0xFF9CA3AF))
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
            Icon(Icons.Filled.ArrowForwardIos, contentDescription = null, tint = Color.White)
        }
    }
}

/* -------------------- PREVIEW -------------------- */
@Preview(showBackground = true)
@Composable
fun CravingAlertPreview() {
    CravingAlertContent(
        onNavigateHome = {},
        onLogCraving = {},
        onBreathing = {},
        onAiChat = {},
        onTimer = {}
    )
}
