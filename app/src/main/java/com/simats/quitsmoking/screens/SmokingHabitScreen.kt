package com.simats.quitsmoking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SmokingHabitScreen(navController: NavController) {

    var cigarettesPerDay by remember { mutableStateOf(20) }
    var costPerPack by remember { mutableStateOf(10) }
    var cigarettesPerPack by remember { mutableStateOf(20) }

    fun adjustValue(value: Int, delta: Int, min: Int = 1): Int {
        return maxOf(min, value + delta)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ---------- HEADER ----------
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Step 2 of 4", color = Color(0xFF0F766E))
            Text(
                "Your Smoking Habit",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "Help us understand your current smoking pattern",
                color = Color.Gray
            )
        }

        // ---------- PROGRESS ----------
        LinearProgressIndicator(
            progress = 0.50f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            color = Color(0xFF0F766E)
        )

        Spacer(Modifier.height(20.dp))

        // ---------- CONTENT ----------
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Cigarettes per day
            HabitBox(
                title = "Cigarettes per day",
                value = cigarettesPerDay.toString(),
                minus = { cigarettesPerDay = adjustValue(cigarettesPerDay, -1) },
                plus = { cigarettesPerDay = adjustValue(cigarettesPerDay, 1) },
                gradient = Brush.linearGradient(
                    listOf(Color(0xFFE0F7F9), Color(0xFFD2F5F4))
                )
            )

            // Cost per pack (NO currency symbol)
            HabitBox(
                title = "Cost per pack",
                value = costPerPack.toString(),
                minus = { costPerPack = adjustValue(costPerPack, -1) },
                plus = { costPerPack = adjustValue(costPerPack, 1) },
                gradient = Brush.linearGradient(
                    listOf(Color(0xFFDFF7E5), Color(0xFFCEFAE1))
                )
            )

            // Cigarettes per pack
            HabitBox(
                title = "Cigarettes per pack",
                value = cigarettesPerPack.toString(),
                minus = { cigarettesPerPack = adjustValue(cigarettesPerPack, -1) },
                plus = { cigarettesPerPack = adjustValue(cigarettesPerPack, 1) },
                gradient = Brush.linearGradient(
                    listOf(Color(0xFFE8F3FF), Color(0xFFD8F3FF))
                )
            )

            // ---------- DAILY COST ----------
            val dailyCost =
                (costPerPack.toFloat() / cigarettesPerPack.toFloat()) * cigarettesPerDay

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFE53935), Color(0xFFFF9800))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Your daily cost",
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        String.format("%.2f", dailyCost),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ---------- CONTINUE ----------
        Button(
            onClick = { navController.navigate("triggers") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0F766E)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Continue", color = Color.White)
        }
    }
}

@Composable
fun HabitBox(
    title: String,
    value: String,
    minus: () -> Unit,
    plus: () -> Unit,
    gradient: Brush
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(title, color = Color(0xFF374151))
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                SmallButton("-", minus)

                Text(
                    value,
                    color = Color(0xFF0D9488),
                    fontWeight = FontWeight.Bold
                )

                SmallButton("+", plus)
            }
        }
    }
}

@Composable
fun SmallButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = Modifier.size(50.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(text, color = Color.Gray)
    }
}
