package com.example.quitsmoking.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

private enum class BreathingPhase { INHALE, HOLD, EXHALE }

@Composable
fun BreathingExerciseScreen(navController: NavController) {

    var isActive by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf(BreathingPhase.INHALE) }
    var count by remember { mutableStateOf(4) }
    var rounds by remember { mutableStateOf(0) }

    // Background gradient
    val backgroundBrush = when (phase) {
        BreathingPhase.INHALE -> Brush.linearGradient(listOf(Color(0xFF60A5FA), Color(0xFF06B6D4)))
        BreathingPhase.HOLD -> Brush.linearGradient(listOf(Color(0xFFA78BFA), Color(0xFFFB7185)))
        BreathingPhase.EXHALE -> Brush.linearGradient(listOf(Color(0xFF2DD4BF), Color(0xFF10B981)))
    }

    // Scale Animation
    val targetScale = when (phase) {
        BreathingPhase.INHALE -> 1.15f
        BreathingPhase.EXHALE -> 0.90f
        BreathingPhase.HOLD -> 1.00f
    }

    val animatedScale by animateFloatAsState(
        targetValue = if (isActive) targetScale else 1f
    )

    // Timer logic
    LaunchedEffect(isActive, phase) {
        if (!isActive) return@LaunchedEffect

        while (isActive) {
            delay(1000L)

            if (count == 1) {
                when (phase) {
                    BreathingPhase.INHALE -> {
                        phase = BreathingPhase.HOLD
                        count = 4
                    }
                    BreathingPhase.HOLD -> {
                        phase = BreathingPhase.EXHALE
                        count = 6
                    }
                    BreathingPhase.EXHALE -> {
                        phase = BreathingPhase.INHALE
                        count = 4
                        rounds += 1
                    }
                }
            } else {
                count -= 1
            }
        }
    }

    fun phaseTitle() = when (phase) {
        BreathingPhase.INHALE -> "Breathe In"
        BreathingPhase.HOLD -> "Hold"
        BreathingPhase.EXHALE -> "Breathe Out"
    }

    fun phaseHint() = when (phase) {
        BreathingPhase.INHALE -> "Fill your lungs slowly..."
        BreathingPhase.HOLD -> "Hold your breath..."
        BreathingPhase.EXHALE -> "Release slowly..."
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {

        // BACK BUTTON
        Box(modifier = Modifier.padding(start = 16.dp, top = 24.dp)) {
            IconButton(onClick = { navController.navigate("craving-alert") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        // MAIN CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Icon bubble
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = phaseTitle(),
                color = Color.White,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Breathing Circle (Scaled)
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .scale(animatedScale),
                contentAlignment = Alignment.Center
            ) {
                // Outer
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                )
                // Middle
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                )
                // Inner
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.35f))
                )

                Text(
                    text = count.toString(),
                    color = Color.White,
                    fontSize = 60.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = phaseHint(),
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PLAY / PAUSE BUTTON
            IconButton(
                onClick = { isActive = !isActive },
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                if (isActive) {
                    Icon(Icons.Default.Pause, contentDescription = null, tint = Color.Black, modifier = Modifier.size(40.dp))
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                color = Color.White.copy(alpha = 0.20f)
            ) {
                Text(
                    text = "Rounds completed: $rounds",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
        }

        // BOTTOM BUTTON
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("craving-success") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("I Feel Better")
            }
        }
    }
}
