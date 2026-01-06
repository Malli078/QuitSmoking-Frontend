package com.example.quitsmoking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class QuitReason(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val bg: Color,
    val tint: Color
)

@Composable
fun WhyQuitScreen(navController: NavController) {

    var selected by remember { mutableStateOf<List<String>>(emptyList()) }

    val reasons = listOf(
        QuitReason("health", "Better Health", Icons.Filled.Favorite, Color(0xFFFFEBEE), Color(0xFFD32F2F)),
        QuitReason("money", "Save Money", Icons.Filled.AttachMoney, Color(0xFFE8F5E9), Color(0xFF2E7D32)),
        QuitReason("family", "For My Family", Icons.Filled.FamilyRestroom, Color(0xFFE3F2FD), Color(0xFF1565C0)),
        QuitReason("fitness", "Improve Fitness", Icons.Filled.FitnessCenter, Color(0xFFF3E5F5), Color(0xFF7B1FA2)),
        QuitReason("pregnancy", "Pregnancy", Icons.Filled.ChildCare, Color(0xFFFCE4EC), Color(0xFFC2185B)),
        QuitReason("lifestyle", "Better Lifestyle", Icons.Filled.SentimentSatisfied, Color(0xFFFFFDE7), Color(0xFFF9A825))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Header
        Text("Step 1 of 4", color = Color(0xFF00897B))
        Text("Why do you want to quit?", style = MaterialTheme.typography.headlineMedium)
        Text("Select all reasons that motivate you", color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        // Progress bar (simple)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.25f)
                    .background(Color(0xFF009688))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Reasons Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reasons) { reason ->
                val isSelected = selected.contains(reason.id)

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFE0F2F1) else Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clickable {
                            selected = if (isSelected)
                                selected - reason.id
                            else
                                selected + reason.id
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        androidx.compose.material3.Icon(
                            imageVector = reason.icon,
                            contentDescription = reason.label,
                            tint = reason.tint,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(reason.label)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        Button(
            onClick = { navController.navigate("smoking_habit") },
            enabled = selected.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}
