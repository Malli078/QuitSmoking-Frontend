package com.example.quitsmoking.screens.health

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.Duration
import java.time.Instant
import kotlin.math.min
import kotlin.math.round
import kotlin.random.Random

/* ---------------- DATA ---------------- */

private data class DayPoint(
    val day: Int,
    val lung: Float,
    val heart: Float,
    val energy: Float
)

/* ---------------- HELPERS ---------------- */

@Composable
private fun whiteCardModifier(): Modifier =
    Modifier
        .shadow(8.dp, RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)

private fun Float.roundToIntSafe(): Int = round(this).toInt()

/* ---------------- SCREEN ---------------- */

@Composable
fun RecoveryGraphScreen(
    navController: NavController,
    quitDateIso: String?
) {
    val daysSinceQuit = remember(quitDateIso) {
        val now = Instant.now()
        val quitInstant = try {
            if (quitDateIso == null) now else Instant.parse(quitDateIso)
        } catch (_: Exception) {
            now
        }
        Duration.between(quitInstant, now).toDays().coerceAtLeast(0).toInt()
    }

    val graphData = remember(daysSinceQuit) {
        val size = min(daysSinceQuit + 1, 30).coerceAtLeast(1)
        (0 until size).map { i ->
            val r = Random(i)
            fun j(a: Float) = (r.nextFloat() - 0.5f) * a
            DayPoint(
                day = i + 1,
                lung = min(100f, 40f + i * 2f + j(5f)),
                heart = min(100f, 50f + i * 1.8f + j(4f)),
                energy = min(100f, 35f + i * 2.2f + j(6f))
            )
        }
    }

    val latest = graphData.last()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {

        /* ---------- HEADER ---------- */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF7C3AED), Color(0xFFF472B6))
                    ),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 48.dp)
            ) {
                Text(
                    "Recovery Graph",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Your health is improving!",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {

            /* ---------- GRAPH CARD ---------- */

            Card(
                modifier = whiteCardModifier().fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp)) {

                    Text(
                        "30-Day Recovery Trends",
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        "${graphData.size} day(s) shown",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(12.dp))

                    RecoveryCanvasGraph(
                        data = graphData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LegendDot("Lung", Color(0xFF06B6D4))
                        Spacer(Modifier.width(12.dp))
                        LegendDot("Heart", Color(0xFFF43F5E))
                        Spacer(Modifier.width(12.dp))
                        LegendDot("Energy", Color(0xFFF59E0B))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            /* ---------- CURRENT STATS ---------- */

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(Modifier.weight(1f), "Lung Function", "${latest.lung.roundToIntSafe()}%")
                StatCard(Modifier.weight(1f), "Heart Health", "${latest.heart.roundToIntSafe()}%")
                StatCard(Modifier.weight(1f), "Energy", "${latest.energy.roundToIntSafe()}%")
            }

            Spacer(Modifier.height(12.dp))

            /* ---------- KEEP GOING ---------- */

            Card(
                modifier = whiteCardModifier().fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Keep Going!",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Your body is recovering faster than you might think. Every day brings new improvements.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        InsightLine("Lung capacity improves by 10% within first month")
                        InsightLine("Circulation normalizes within 2–12 weeks")
                        InsightLine("Energy levels increase significantly by week 4")
                    }
                }
            }
        }
    }
}

/* ---------------- ANIMATED GRAPH ---------------- */

@Composable
private fun RecoveryCanvasGraph(
    data: List<DayPoint>,
    modifier: Modifier
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(900))
    }

    val padding = with(LocalDensity.current) { 8.dp.toPx() }

    Canvas(modifier = modifier) {
        drawRect(Color(0xFFF1F5F9))

        val w = size.width
        val h = size.height
        val left = padding
        val right = w - padding
        val top = padding
        val bottom = h - padding

        val pw = right - left
        val ph = bottom - top

        fun x(i: Int) =
            if (data.size == 1) (left + right) / 2
            else left + pw * (i.toFloat() / (data.size - 1))

        fun y(v: Float) =
            bottom - ph * (v.coerceIn(0f, 100f) / 100f)

        repeat(5) {
            val gy = top + ph * it / 4f
            drawLine(Color(0xFFE5E7EB), Offset(left, gy), Offset(right, gy), 1f)
        }

        val stroke = Stroke(2.4f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        if (data.size == 1) {
            val a = progress.value
            drawCircle(Color(0xFF06B6D4), 6f, Offset(x(0), y(data[0].lung)), a)
            drawCircle(Color(0xFFF43F5E), 6f, Offset(x(0), y(data[0].heart)), a)
            drawCircle(Color(0xFFF59E0B), 6f, Offset(x(0), y(data[0].energy)), a)
            return@Canvas
        }

        fun animatedPath(values: List<Float>): Path {
            val path = Path()
            val maxIndex = ((values.size - 1) * progress.value)
                .toInt()
                .coerceAtLeast(1)

            values.take(maxIndex + 1).forEachIndexed { i, v ->
                if (i == 0) path.moveTo(x(i), y(v)) else path.lineTo(x(i), y(v))
            }
            return path
        }

        drawPath(animatedPath(data.map { it.lung }), Color(0xFF06B6D4), style = stroke)
        drawPath(animatedPath(data.map { it.heart }), Color(0xFFF43F5E), style = stroke)
        drawPath(animatedPath(data.map { it.energy }), Color(0xFFF59E0B), style = stroke)
    }
}

/* ---------------- SMALL COMPONENTS ---------------- */

@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(10.dp)) { drawCircle(color) }
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Black)
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    label: String,
    value: String
) {
    Card(
        modifier = whiteCardModifier().then(modifier),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Black)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
    }
}

@Composable
private fun InsightLine(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text("•", color = Color.Black)
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color.Black)
    }
}
