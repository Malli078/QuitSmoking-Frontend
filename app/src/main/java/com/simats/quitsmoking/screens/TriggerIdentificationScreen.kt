package com.simats.quitsmoking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun TriggerIdentificationScreen(navController: NavController) {

    var selectedTriggers by remember { mutableStateOf(listOf<String>()) }

    data class Trigger(
        val id: String,
        val label: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val bgColor: Color
    )

    val triggers = listOf(
        Trigger("coffee", "After Coffee", Icons.Filled.Coffee, Color(0xFFFFF2CC)),
        Trigger("alcohol", "With Alcohol", Icons.Filled.WineBar, Color(0xFFEBD5FF)),
        Trigger("social", "Social Events", Icons.Filled.Groups, Color(0xFFD8E6FF)),
        Trigger("stress", "When Stressed", Icons.Filled.MoodBad, Color(0xFFFFD6D6)),
        Trigger("driving", "While Driving", Icons.Filled.DirectionsCar, Color(0xFFF2F2F2)),
        Trigger("breaks", "During Breaks", Icons.Filled.AccessTime, Color(0xFFDAF5DA))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ---------- HEADER ----------
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp)) {
            Text(
                "Step 3 of 4",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Identify Your Triggers",
                fontSize = 24.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "When do you typically smoke?",
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        Spacer(Modifier.height(20.dp))

        // ---------- PROGRESS ----------
        LinearProgressIndicator(
            progress = 0.75f,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            color = Color.Black
        )

        Spacer(Modifier.height(20.dp))

        // ---------- TRIGGERS ----------
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            triggers.forEach { trigger ->
                val isSelected = selectedTriggers.contains(trigger.id)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            if (isSelected) Color(0xFFE0F7F7) else Color.White,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            selectedTriggers =
                                if (isSelected) selectedTriggers - trigger.id
                                else selectedTriggers + trigger.id
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Icon box
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(trigger.bgColor, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            trigger.icon,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    // Label
                    Text(
                        trigger.label,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    // Checkmark
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(Color.Black, RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // ---------- INFO ----------
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Text(
                "We'll help you prepare for these situations with personalized strategies",
                color = Color.Black,
                fontSize = 14.sp
            )
        }

        // ---------- CONTINUE ----------
        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("triggers") { inclusive = true }
                }
            },
            enabled = selectedTriggers.isNotEmpty(),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                "Continue",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
