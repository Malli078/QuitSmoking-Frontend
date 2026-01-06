package com.example.quitsmoking.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(navController: NavController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val features = listOf(
        Triple(Icons.Filled.Favorite, "Track your health recovery in real-time", Color(0xFFE53935)),
        Triple(Icons.Filled.TrendingUp, "Beat cravings with AI support", Color(0xFF1E88E5)),
        Triple(Icons.Filled.AutoAwesomeMotion, "Earn badges and celebrate milestones", Color(0xFFFFC107)),
        Triple(Icons.Filled.AutoAwesome, "Get personalized quit strategies", Color(0xFF8E24AA)),
    )

    val headerGradient = Brush.verticalGradient(
        listOf(Color(0xFF00897B), Color(0xFF00ACC1))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE0F2F1), Color(0xFFE0F7FA))
                )
            )
    ) {

        // ---------- HEADER ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(headerGradient)
                .padding(16.dp)
        ) {

            TextButton(
                onClick = {
                    prefs.edit().putBoolean("block_auto_login", true).apply()
                    navController.navigate("login")
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("Skip", color = Color.White)
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFF00897B),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Welcome to QuitSmart",
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    "Your personal companion for a smoke-free life",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ✅ OPTION 2 — Spacer to push cards DOWN
        Spacer(modifier = Modifier.height(24.dp))

        // ---------- FEATURES ----------
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)
                // ✅ OPTION 1 — Extra top padding (fine adjustment)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            features.forEach {
                FeatureCard(
                    icon = it.first,
                    text = it.second,
                    tint = it.third
                )
            }
        }

        // ---------- ACTIONS ----------
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = { navController.navigate("why_quit") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
            ) {
                Text("Get Started", color = Color.White)
            }

            OutlinedButton(
                onClick = {
                    prefs.edit().putBoolean("block_auto_login", true).apply()
                    navController.navigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Already Have an Account? Log In", color = Color(0xFF00897B))
            }
        }
    }
}

@Composable
fun FeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    tint: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp), // ✅ SAME SIZE
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = text,
                color = Color(0xFF263238),
                fontSize = 14.sp
            )
        }
    }
}
