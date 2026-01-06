package com.example.quitsmoking.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CravingSeverityScreen(navController: NavController) {
    var severity by rememberSaveable { mutableStateOf(5) }

    fun getSeverityBrush(): Brush {
        return when {
            severity <= 3 -> Brush.linearGradient(listOf(Color(0xFF86efac), Color(0xFF10b981)))
            severity <= 6 -> Brush.linearGradient(listOf(Color(0xFFFDE68A), Color(0xFFF97316)))
            else -> Brush.linearGradient(listOf(Color(0xFFF97316), Color(0xFFEF4444)))
        }
    }

    fun getSeverityText(): String {
        return when {
            severity <= 3 -> "Mild"
            severity <= 6 -> "Moderate"
            else -> "Intense"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Craving severity") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 2.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { navController.navigate("craving_type") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(40.dp)
                    ) {
                        Text(text = "Continue")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = "Continue")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "How intense is your craving?",
                    color = Color(0xFF111827),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Rate it from 1 to 10",
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(192.dp)
                        .clip(CircleShape)
                        .background(brush = getSeverityBrush())
                        .shadow(8.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = severity.toString(), color = Color.White, fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = getSeverityText(), color = Color.White, fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Slider(
                    value = severity.toFloat(),
                    onValueChange = { severity = it.toInt().coerceIn(1, 10) },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "1 - Mild", color = Color(0xFF6B7280), fontSize = 12.sp)
                    Text(text = "10 - Intense", color = Color(0xFF6B7280), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..10).forEach { num ->
                        val selected = num == severity
                        Button(
                            onClick = { severity = num },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) Color(0xFFDC2626) else Color(0xFFF3F4F6),
                                contentColor = if (selected) Color.White else Color(0xFF4B5563)
                            ),
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = if (selected) 6.dp else 0.dp)
                        ) {
                            Text(text = num.toString(), fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
