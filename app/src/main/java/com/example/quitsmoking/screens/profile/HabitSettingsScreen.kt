package com.example.quitsmoking.screens.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quitsmoking.viewmodel.HabitViewModel

@Composable
fun HabitSettingsScreen(
    navController: NavController,
    viewModel: HabitViewModel = viewModel()
) {
    val context = LocalContext.current

    val userPrefs =
        context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    val userId = userPrefs.getInt("user_id", 0)

    val prefs =
        context.getSharedPreferences("user_habits", Context.MODE_PRIVATE)

    var cigarettesPerDay by remember { mutableStateOf(10) }
    var costPerPack by remember { mutableStateOf(10) }
    var cigarettesPerPack by remember {
        mutableStateOf(prefs.getInt("cigarettes_per_pack", 20))
    }

    val habits by viewModel.habits.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        if (userId > 0) {
            viewModel.loadHabits(userId)
        }
    }

    LaunchedEffect(habits) {
        habits?.let {
            cigarettesPerDay = it.cigarettes_per_day
            costPerPack = it.cost_per_pack.toInt()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.Black)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                "Habit Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Text(
                "Adjust your smoking habits",
                color = Color.Black
            )
            Spacer(Modifier.height(20.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {

            // 🚬 Cigarettes Per Day
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Cigarettes Per Day", color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        IconButton(
                            onClick = {
                                cigarettesPerDay = maxOf(1, cigarettesPerDay - 1)
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE5E7EB))
                        ) {
                            Icon(Icons.Default.Remove, null, tint = Color.Black)
                        }

                        Spacer(Modifier.width(20.dp))

                        Text(
                            cigarettesPerDay.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black
                        )

                        Spacer(Modifier.width(20.dp))

                        IconButton(
                            onClick = { cigarettesPerDay++ },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE5E7EB))
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.Black)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 💰 Cost Per Pack
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Cost Per Pack", color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = costPerPack.toString(),
                        onValueChange = {
                            costPerPack = it.toIntOrNull() ?: costPerPack
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // 📦 Cigarettes Per Pack
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Cigarettes Per Pack", color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf(20, 25).forEach { value ->
                            Button(
                                onClick = { cigarettesPerPack = value },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        if (cigarettesPerPack == value)
                                            Color(0xFF009966)
                                        else Color(0xFFE5E7EB)
                                )
                            ) {
                                Text(
                                    value.toString(),
                                    color =
                                        if (cigarettesPerPack == value)
                                            Color.White
                                        else Color.Black
                                )
                            }
                        }
                    }
                }
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = Color.Red)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    viewModel.saveHabits(
                        userId = userId,
                        cigarettesPerDay = cigarettesPerDay,
                        costPerPack = costPerPack.toDouble(),
                        quitDate = null
                    ) {
                        prefs.edit()
                            .putInt("cigarettes_per_day", cigarettesPerDay)
                            .putInt("cost_per_pack", costPerPack)
                            .putInt("cigarettes_per_pack", cigarettesPerPack)
                            .apply()

                        navController.navigate("profile") {
                            popUpTo("habit_settings") { inclusive = true }
                        }
                    }
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF009966))
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Save Changes", color = Color.White)
                }
            }
        }
    }
}
