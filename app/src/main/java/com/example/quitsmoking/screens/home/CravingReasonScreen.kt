package com.example.quitsmoking.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class Reason(val id: String, val label: String, val iconType: ReasonIconType)
private enum class ReasonIconType { Stress, Coffee, Social, Work, Alcohol, Boredom, Location }

@Composable
fun CravingReasonScreen(navController: NavController) {
    val reasons = listOf(
        Reason("stress", "Stress", ReasonIconType.Stress),
        Reason("coffee", "Coffee/Tea", ReasonIconType.Coffee),
        Reason("social", "Social Setting", ReasonIconType.Social),
        Reason("work", "Work Pressure", ReasonIconType.Work),
        Reason("alcohol", "Alcohol", ReasonIconType.Alcohol),
        Reason("boredom", "Boredom", ReasonIconType.Boredom),
        Reason("location", "Specific Place", ReasonIconType.Location)
    )

    // Using a mutableStateList to add/remove selections
    val selected = remember { mutableStateListOf<String>() }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top area with back button
            Box(modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Text(text = "What triggered it?", fontSize = 22.sp, color = Color(0xFF111827))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Select all that apply", fontSize = 14.sp, color = Color(0xFF6B7280))

                Spacer(modifier = Modifier.height(18.dp))

                val chunks = reasons.chunked(2)
                chunks.forEach { pair ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        pair.forEach { reason ->
                            val isSelected = selected.contains(reason.id)
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(if (isSelected) Color(0xFF10B981) else Color.White)
                                    .clickable {
                                        if (isSelected) selected.remove(reason.id) else selected.add(reason.id)
                                    }
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                                    when (reason.iconType) {
                                        ReasonIconType.Stress -> Icon(Icons.Default.SentimentDissatisfied, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFFDC2626))
                                        ReasonIconType.Coffee -> Icon(Icons.Default.Coffee, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFFB45309))
                                        ReasonIconType.Social -> Icon(Icons.Default.People, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFF1E3A8A))
                                        ReasonIconType.Work -> Icon(Icons.Default.Work, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFF6D28D9))
                                        ReasonIconType.Alcohol -> Icon(Icons.Default.WineBar, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFFDB2777))
                                        ReasonIconType.Boredom -> Icon(Icons.Default.AccessTime, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFF6B7280))
                                        ReasonIconType.Location -> Icon(Icons.Default.Place, contentDescription = null, tint = if (isSelected) Color.White else Color(0xFF16A34A))
                                    }

                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.TopEnd)
                                                .offset(x = 12.dp, y = (-12).dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = reason.label, color = if (isSelected) Color.White else Color(0xFF111827))
                            }
                        }

                        if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(96.dp)) // give space for footer
            }

            // Footer (fixed at bottom)
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                val enabled = selected.isNotEmpty()
                Button(
                    onClick = { if (enabled) navController.navigate("craving-timer") },
                    enabled = enabled,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(40.dp)
                ) {
                    Text(text = "Continue")
                }
            }
        }
    }
}
