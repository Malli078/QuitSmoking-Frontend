package com.simats.quitsmoking.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class Symptom(val id: String, val label: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomsBetterScreen(navController: NavController) {

    val improvements = remember { mutableStateListOf<String>() }

    val symptoms = listOf(
        Symptom("cough", "Less coughing"),
        Symptom("breathing", "Easier breathing"),
        Symptom("energy", "More energy"),
        Symptom("sleep", "Better sleep"),
        Symptom("smell", "Improved sense of smell"),
        Symptom("taste", "Better taste"),
        Symptom("anxiety", "Reduced anxiety"),
        Symptom("skin", "Healthier skin")
    )

    fun toggle(id: String) {
        if (improvements.contains(id)) improvements.remove(id)
        else improvements.add(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp
            ) {
                Button(
                    onClick = { navController.navigate("health_dashboard") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Save Progress")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Text("What's Better?", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text("Track your symptom improvements", color = Color.Gray)
            Spacer(Modifier.height(16.dp))

            // Summary Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFE8FFF5), Color(0xFFDFFEF4))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE0FDF4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.SentimentSatisfied,
                                contentDescription = null,
                                tint = Color(0xFF059669)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text("Great Job!", style = MaterialTheme.typography.titleMedium)

                            val count = improvements.size
                            val msg = if (count == 0)
                                "Select symptoms that have improved"
                            else
                                "You've noticed $count improvement${if (count > 1) "s" else ""}!"

                            Text(msg, color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(symptoms) { item ->
                    val selected = improvements.contains(item.id)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { toggle(item.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) Color(0xFF059669)
                            else Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (selected) 6.dp else 2.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(if (selected) Color.White else Color.Transparent)
                                        .border(
                                            width = if (selected) 0.dp else 1.dp,
                                            color = if (selected) Color.Transparent else Color.LightGray,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }

                                if (selected) {
                                    Icon(
                                        Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = item.label,
                                color = if (selected) Color.White else Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
