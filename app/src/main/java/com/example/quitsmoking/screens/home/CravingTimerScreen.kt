package com.example.quitsmoking.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun CravingTimerScreen(navController: NavController) {

    var timeLeft by remember { mutableStateOf(300) }   // 5 minutes
    var isActive by remember { mutableStateOf(false) }

    val tips = listOf(
        "💧 Drink a glass of water slowly",
        "🚶 Take a quick walk around the block",
        "🫁 Practice deep breathing",
        "📱 Call a supportive friend",
        "🎮 Play a quick game",
        "🧘 Try a 2-minute meditation"
    )

    val elapsed = 300 - timeLeft
    val currentTip = tips[(elapsed / 50).coerceIn(0, tips.size - 1)]

    val progress = (elapsed.toFloat() / 300f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(progress)

    // TIMER LOGIC
    LaunchedEffect(isActive, timeLeft) {
        if (isActive && timeLeft > 0) {
            delay(1000)
            timeLeft -= 1
        } else if (timeLeft == 0 && isActive) {
            isActive = false
            delay(1000)
            navController.navigate("craving-success")
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeFormatted = String.format("%d:%02d", minutes, seconds)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0F766E) // teal-like background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // BACK BUTTON
            Box(modifier = Modifier.padding(start = 16.dp, top = 24.dp)) {
                IconButton(onClick = { navController.navigate("craving-alert") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Timer Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Ride Out the Craving", color = Color.White, fontSize = 22.sp)
                Text(
                    "Most cravings pass in 3–5 minutes",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // PROGRESS CIRCLE
                Box(modifier = Modifier.size(260.dp), contentAlignment = Alignment.Center) {

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 8.dp.toPx()
                        val radius = (size.minDimension / 2) - stroke / 2
                        val center = Offset(size.width / 2, size.height / 2)

                        // Background circle
                        drawCircle(
                            color = Color.White.copy(alpha = 0.25f),
                            radius = radius,
                            center = center,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )

                        // Progress arc
                        drawArc(
                            color = Color.White,
                            startAngle = -90f,
                            sweepAngle = 360 * animatedProgress,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(timeFormatted, color = Color.White, fontSize = 42.sp)
                        Text(
                            if (timeLeft > 0) "remaining" else "Complete!",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // PLAY / PAUSE
                IconButton(
                    onClick = { isActive = !isActive },
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    if (isActive) {
                        Icon(Icons.Default.Pause, contentDescription = "Pause", tint = Color(0xFF0F766E), modifier = Modifier.size(40.dp))
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = Color(0xFF0F766E), modifier = Modifier.size(40.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tip Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("While you wait...", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(currentTip, color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }

            // BOTTOM BUTTON
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Button(
                    onClick = { navController.navigate("craving-success") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF0F766E)
                    )
                ) {
                    Text("I Feel Better Now")
                }
            }
        }
    }
}
