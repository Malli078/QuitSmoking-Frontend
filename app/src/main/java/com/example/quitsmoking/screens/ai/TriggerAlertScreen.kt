package com.example.quitsmoking.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TriggerAlertScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFFF8A3D), Color(0xFFDC2626))
                )
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {

        // Close button
        IconButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Trigger Detected!",
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "You're in a situation that usually triggers a craving. Stay strong!",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(20.dp)
            ) {
                InfoRow("Trigger Type", "Social Setting")
                InfoRow("Location", "Coffee Shop")
                InfoRow("Risk Level", "High")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // BREATHING
                ActionButton(
                    bg = Color.White,
                    contentColor = Color(0xFFFF7F2A),
                    icon = Icons.Filled.Air,
                    title = "Quick Breathing Exercise"
                ) {
                    navController.navigate("breathing-exercise")
                }

                // AI SUPPORT  ✅ FIXED HERE
                ActionButton(
                    bg = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White,
                    icon = Icons.AutoMirrored.Filled.Message,
                    title = "Get AI Support"
                ) {
                    navController.navigate("ai_chat")
                }

                // DISTRACTION
                ActionButton(
                    bg = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White,
                    icon = Icons.Filled.Shield,
                    title = "Use Distraction Tools"
                ) {
                    navController.navigate("distraction-tools")
                }

                // DISMISS
                TextButton(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("I'm Okay, Dismiss", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White.copy(alpha = 0.85f))
        Text(value, color = Color.White)
    }
}

@Composable
fun ActionButton(
    bg: Color,
    contentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = contentColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(50.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontSize = 15.sp)
    }
}
