package com.simats.quitsmoking.screens.home

import android.content.Intent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

data class Quote(val text: String, val author: String)

private val quotes = listOf(
    Quote("Every moment you resist is a victory. 🌱", "QuitSmart Team"),
    Quote("You are stronger than your cravings. 💪", "Recovery Wisdom"),
    Quote("The best time to quit was yesterday. The second best time is now. ⏰", "Chinese Proverb"),
    Quote("Your lungs thank you with every breath. 🌬️", "Health Expert"),
    Quote("This time, make it the last. 🔥", "Mark Twain (adapted)")
)

data class Raindrop(var x: Float, var y: Float, val length: Float, val speed: Float)

@Composable
fun DailyMotivationScreen(navController: NavController) {
    val context = LocalContext.current
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    var liked by rememberSaveable { mutableStateOf(false) }
    val currentQuote = quotes[currentIndex]

    val gradientColors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
    val drops = remember {
        List(70) {
            Raindrop(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                length = Random.nextInt(10, 25).toFloat(),
                speed = Random.nextFloat() * 5 + 3
            )
        }
    }

    val transition = rememberInfiniteTransition()
    val rainOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing))
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            /* ---------- Background Gradient + Rain ---------- */
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                drawRect(brush = Brush.verticalGradient(gradientColors), size = size)

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "MOTIVATION", width * 0.05f, height * 0.3f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.argb(25, 255, 255, 255)
                            textSize = 160f
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }
                    )
                    drawText(
                        "FOCUS", width * 0.5f, height * 0.7f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.argb(20, 255, 255, 255)
                            textSize = 140f
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }
                    )
                }

                val rainColor = Color.White.copy(alpha = 0.3f)
                drops.forEach { drop ->
                    val newY = (drop.y * height + rainOffset * drop.speed * 100) % height
                    drawLine(
                        color = rainColor,
                        start = Offset(drop.x * width, newY),
                        end = Offset(drop.x * width, newY + drop.length),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }

            /* ---------- Foreground Content ---------- */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                // ✅ Properly aligned Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color.White.copy(alpha = 0.12f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Daily Motivation",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Center content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (liked) Color(0xFFEF4444) else Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "✨ Daily Motivation ✨",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                            .padding(20.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "\"${currentQuote.text}\"",
                                color = Color.White,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "— ${currentQuote.author}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IconButton(
                            onClick = { liked = !liked },
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (liked) Color(0xFFEF4444) else Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                currentIndex = (currentIndex + 1) % quotes.size
                                liked = false
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Next", tint = Color.White)
                        }

                        IconButton(
                            onClick = {
                                val shareText = "\"${currentQuote.text}\" — ${currentQuote.author}"
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(intent, "Share quote"))
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White)
                        }
                    }
                }

                // Bottom info bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💧 You've read ${currentIndex + 1} motivational quotes today!",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
