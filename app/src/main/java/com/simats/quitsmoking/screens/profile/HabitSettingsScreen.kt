package com.simats.quitsmoking.screens.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simats.quitsmoking.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitSettingsScreen(
    navController: NavController,
    viewModel: HabitViewModel = viewModel()
) {
    val context = LocalContext.current

    val userPrefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    val userId = userPrefs.getInt("user_id", 0)

    val prefs = context.getSharedPreferences("user_habits", Context.MODE_PRIVATE)

    var cigarettesPerDay by remember { mutableStateOf(10) }
    var costPerPack by remember { mutableStateOf(10) }
    var cigarettesPerPack by remember {
        mutableStateOf(prefs.getInt("cigarettes_per_pack", 20))
    }

    // ✅ Proper collectAsState() import
    val habits by viewModel.habits.collectAsState(initial = null)
    val loading by viewModel.loading.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = null)

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

        /* ---------- Header ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
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
                    .padding(top = 40.dp, start = 8.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Habit Settings",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 22.sp
                )
                Text(
                    text = "Adjust your smoking habits",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        /* ---------- Main Content ---------- */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .background(Color(0xFFF3F4F6))
        ) {

            // 🚬 Cigarettes Per Day
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Cigarettes Per Day", color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                cigarettesPerDay = maxOf(1, cigarettesPerDay - 1)
                            },
                            modifier = Modifier
                                .size(42.dp)
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
                                .size(42.dp)
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Cost Per Pack", color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = costPerPack.toString(),
                        onValueChange = {
                            costPerPack = it.toIntOrNull() ?: costPerPack
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // 📦 Cigarettes Per Pack
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
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
                                            Color(0xFF10B981)
                                        else Color(0xFFE5E7EB)
                                ),
                                shape = RoundedCornerShape(12.dp)
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

            if (error != null) {
                Spacer(Modifier.height(12.dp))
                Text(error!!, color = Color.Red)
            }

            Spacer(Modifier.height(24.dp))

            // 💾 Save Button
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
                colors = ButtonDefaults.buttonColors(Color(0xFF10B981))
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
