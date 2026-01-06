package com.example.quitsmoking.screens.profile

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    var name by remember { mutableStateOf(prefs.getString("name", "") ?: "") }
    var email by remember { mutableStateOf(prefs.getString("email", "") ?: "") }
    var quitDate by remember { mutableStateOf(prefs.getString("quit_date", "") ?: "") }

    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Spacer(Modifier.height(8.dp))
        Text("Edit Profile", style = MaterialTheme.typography.headlineMedium)
        Text("Update your information", color = Color.Gray)

        Spacer(Modifier.height(24.dp))

        /* ---------- AVATAR ---------- */
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(Color(0xFF009970)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.uppercase() ?: "U",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- NAME ---------- */
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(Modifier.height(16.dp))

        /* ---------- EMAIL ---------- */
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, null) },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(Modifier.height(16.dp))

        /* ---------- QUIT DATE (FUTURE ONLY) ---------- */
        OutlinedTextField(
            value = quitDate,
            onValueChange = {},
            enabled = false,
            label = { Text("Quit Date (Future only)") },
            leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, y, m, d ->
                            calendar.set(y, m, d)
                            if (calendar.time.after(Date())) {
                                quitDate = formatter.format(calendar.time)
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.minDate = System.currentTimeMillis() + 86400000
                    }.show()
                }
        )

        Text(
            "Quit date must be in the future",
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = Color.Gray
        )

        Spacer(Modifier.weight(1f))

        /* ---------- SAVE ---------- */
        Button(
            onClick = {
                prefs.edit()
                    .putString("name", name)
                    .putString("email", email)
                    .putString("quit_date", quitDate)
                    .apply()

                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(Color(0xFF009970))
        ) {
            Text("Save Changes", color = Color.White)
        }
    }
}
