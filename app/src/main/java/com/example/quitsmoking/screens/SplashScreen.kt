package com.example.quitsmoking.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SplashScreen(navController: NavController) {

    // Auto-navigate after delay
    LaunchedEffect(Unit) {
        // Navigate to welcome screen after 2.5 seconds
        kotlinx.coroutines.delay(2500)
        navController.navigate("welcome") {
            popUpTo("splash") { inclusive = true }
        }
    }

    // infinite rotation animation for circle
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(3000, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    // Background gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0D9488), // teal
                        Color(0xFF0891B2), // cyan
                        Color(0xFF2563EB)  // blue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Rotating circle
        Box(
            modifier = Modifier
                .size(180.dp)
                .rotate(rotation)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
        )

        // Logo circle
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🚭",
                fontSize = 48.sp,
                color = Color.Red
            )
        }

        // App name
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "QuitSmart",
                fontSize = 28.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Your Journey to Freedom",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        // Simple loading dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(3) { index ->
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.5f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        tween(600, easing = LinearEasing, delayMillis = index * 200),
                        RepeatMode.Reverse
                    )
                )

                Box(
                    modifier = Modifier
                        .size(10.dp * scale)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}
