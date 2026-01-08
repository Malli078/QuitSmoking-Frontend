@file:OptIn(ExperimentalMaterial3Api::class)

package com.simats.quitsmoking.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlin.math.roundToInt
import java.util.*

/* ---------- MAIN SCREEN ---------- */
@Composable
fun TodaysGoalScreen(navController: NavController) {

    val goalsState = remember {
        mutableStateListOf(
            Goal("1", "Morning breathing exercise", true, "8:00 AM"),
            Goal("2", "Log afternoon check-in", false, "1:00 PM"),
            Goal("3", "Evening gratitude journal", false, "8:00 PM"),
            Goal("4", "Track water intake (8 glasses)", false, null),
            Goal("5", "Read quit smoking tip of the day", true, null),
            Goal("6", "10-minute walk or exercise", false, "6:00 PM"),
            Goal("7", "Avoid trigger situations", true, null),
            Goal("8", "Update health metrics", false, null)
        )
    }

    val completedCount by remember {
        derivedStateOf { goalsState.count { it.completed } }
    }

    val progressFraction by remember {
        derivedStateOf {
            if (goalsState.isNotEmpty())
                completedCount.toFloat() / goalsState.size
            else 0f
        }
    }

    val animatedProgress = animateFloatAsState(progressFraction).value
    val progressPercent = (animatedProgress * 100).roundToInt()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF10B981),
                    titleContentColor = Color.White
                ),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Today's Goals", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "$completedCount of ${goalsState.size} completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "Add Goal",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFFFFF8E7)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // ---- Progress Card ----
            ProgressCard(animatedProgress, progressPercent)

            Spacer(modifier = Modifier.height(12.dp))

            // ---- ✅ Scrollable Goals List ----
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // enables scrolling properly
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(goalsState, key = { it.id }) { goal ->
                    GoalCard(goal = goal) {
                        val index = goalsState.indexOfFirst { it.id == goal.id }
                        if (index >= 0) {
                            goalsState[index] =
                                goalsState[index].copy(completed = !goal.completed)
                        }
                    }
                }
            }
        }

        // ---- Add Goal Dialog ----
        if (showDialog) {
            AddGoalDialog(
                onDismiss = { showDialog = false },
                onAddGoal = { title, time ->
                    goalsState.add(
                        Goal(
                            id = UUID.randomUUID().toString(),
                            text = title,
                            completed = false,
                            time = time.ifBlank { null }
                        )
                    )
                    showDialog = false
                }
            )
        }
    }
}

/* ---------- GOAL CARD ---------- */
@Composable
fun GoalCard(goal: Goal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (goal.completed)
                    Icons.Filled.CheckCircle
                else Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (goal.completed)
                    Color(0xFF10B981)
                else Color(0xFF9CA3AF),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    goal.text,
                    color = if (goal.completed)
                        Color(0xFF9CA3AF)
                    else Color(0xFF111827),
                    textDecoration = if (goal.completed)
                        TextDecoration.LineThrough
                    else TextDecoration.None
                )
                goal.time?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(it, color = Color.Gray)
                }
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF9CA3AF)
            )
        }
    }
}

/* ---------- PROGRESS CARD ---------- */
@Composable
fun ProgressCard(animatedProgress: Float, progressPercent: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Daily Progress", color = Color.Gray)
                Text("$progressPercent%", color = Color(0xFF059669))
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF059669),
                trackColor = Color(0xFFE5E7EB)
            )
        }
    }
}

/* ---------- ADD GOAL DIALOG ---------- */
@Composable
fun AddGoalDialog(onDismiss: () -> Unit, onAddGoal: (String, String) -> Unit) {
    var newGoalText by remember { mutableStateOf("") }
    var newGoalTime by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add New Goal", color = Color(0xFF059669))

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newGoalText,
                    onValueChange = { newGoalText = it },
                    label = { Text("Goal Title") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = newGoalTime,
                    onValueChange = { newGoalTime = it },
                    label = { Text("Time (optional)") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (newGoalText.isNotBlank()) {
                                onAddGoal(newGoalText.trim(), newGoalTime)
                                newGoalText = ""
                                newGoalTime = ""
                            }
                        }
                    ) {
                        Text("Add Goal")
                    }
                }
            }
        }
    }
}

/* ---------- MODEL ---------- */
data class Goal(
    val id: String,
    val text: String,
    val completed: Boolean,
    val time: String? = null
)
