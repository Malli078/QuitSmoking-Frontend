package com.simats.quitsmoking.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.roundToInt
import kotlin.math.max
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Composable
fun MoneySavedScreen(
    navController: NavController,
    userQuitDateEpochMillis: Long? = null,
    cigarettesPerDay: Int = 10,
    costPerPack: Double = 10.0
) {
    val today = LocalDate.now(ZoneId.systemDefault())
    val quitDate = if (userQuitDateEpochMillis != null) {
        Instant.ofEpochMilli(userQuitDateEpochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } else today

    val daysSinceQuit = max(0, ChronoUnit.DAYS.between(quitDate, today).toInt())

    val cigarettesPerPack = 20.0
    val dailySavings = (cigarettesPerDay / cigarettesPerPack) * costPerPack
    val moneySaved = (dailySavings * daysSinceQuit).roundToInt()
    val monthlySavings = (dailySavings * 30).roundToInt()
    val yearlySavings = (dailySavings * 365).roundToInt()

    val affordableItems = listOf(
        AffordableItem("Coffee runs for a month", 150, Icons.Default.LocalCafe),
        AffordableItem("Weekend getaway", 500, Icons.Default.Flight),
        AffordableItem("New smartphone", 800, Icons.Default.PhoneIphone),
        AffordableItem("Dream vacation", 2000, Icons.Default.FlightTakeoff),
        AffordableItem("Down payment savings", 5000, Icons.Default.Home)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
    ) {

        /* ---------- HEADER ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF10B981), Color(0xFF059669))
                    )
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 42.dp, start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Money Saved",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Watch your savings grow every day!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ---------- TOTAL SAVINGS CARD ---------- */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total Saved", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "₹$moneySaved",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            "In $daysSinceQuit days",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF34D399), Color(0xFF059669))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Stat("Daily", dailySavings.roundToInt())
                    Stat("Monthly", monthlySavings)
                    Stat("Yearly", yearlySavings)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- AFFORDABLE ITEMS ---------- */
        Text(
            "What You Could Afford",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            affordableItems.forEach { item ->
                val canAfford = moneySaved >= item.cost
                val progress = (moneySaved.toDouble() / item.cost).coerceAtMost(1.0)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFE5E7EB), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(item.icon, null, tint = Color.Black)
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, color = Color.Black, fontWeight = FontWeight.Medium)
                                Text(
                                    "₹${item.cost}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }

                            if (canAfford) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE5E7EB))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress.toFloat())
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF10B981))
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

/* ---------- STATS COMPONENT ---------- */
@Composable
private fun Stat(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text("₹$value", color = Color.Black, fontWeight = FontWeight.Bold)
    }
}

/* ---------- DATA MODEL ---------- */
private data class AffordableItem(
    val name: String,
    val cost: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
