package com.example.quitsmoking.screens

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quitsmoking.viewmodel.RegisterViewModel

@Composable
fun CreateAccountScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    fun validate(): Boolean {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            localError = "All fields are required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            localError = "Invalid email format"
            return false
        }
        if (password.length < 6) {
            localError = "Password must be at least 6 characters"
            return false
        }
        localError = ""
        return true
    }

    // navigation after registration
    LaunchedEffect(viewModel.success.value) {
        if (viewModel.success.value) {
            navController.navigate("login") {
                popUpTo("create_account") { inclusive = true }
            }
            viewModel.success.value = false
        }
    }

    // background animation
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // smoke background
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://as2.ftcdn.net/v2/jpg/10/51/32/77/1000_F_1051327760_G7G4vuZxAwYcBDEcbHL5PGy9nRDaH8Pw.jpg")
                .crossfade(true)
                .build(),
            contentDescription = "Stop Smoking Background",
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
                .alpha(0.6f)
                .offset(y = offsetY.dp),
            contentScale = ContentScale.Crop
        )

        // blurred glass box
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.LightGray.copy(alpha = 0.25f)
                        )
                    )
                )
                .blur(0.dp)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // circular logo
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                        .blur(0.dp)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://as2.ftcdn.net/v2/jpg/15/22/23/95/1000_F_1522239509_1J9sPLsIINyK7sU8VfEAD1hH5vZVgAVZ.jpg")
                            .crossfade(true)
                            .build(),
                        contentDescription = "No Smoking Icon",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Create Account",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(24.dp))

                // full name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", color = Color.Black) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )

                Spacer(Modifier.height(12.dp))

                // email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.Black) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )

                Spacer(Modifier.height(12.dp))

                // password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.Black) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )

                if (localError.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(localError, color = Color.Red)
                }
                if (viewModel.error.value.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(viewModel.error.value, color = Color.Red)
                }

                Spacer(Modifier.height(20.dp))

                // create account button
                Button(
                    onClick = {
                        if (validate()) {
                            saveUserName(context, name)
                            viewModel.register(name, email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !viewModel.loading.value
                ) {
                    if (viewModel.loading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Create Account", color = Color.White)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row {
                    Text("Already have an account? ", color = Color.Black)
                    Text(
                        text = "Sign In",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C),
                        modifier = Modifier.clickable {
                            navController.navigate("login")
                        }
                    )
                }
            }
        }
    }
}
private fun saveUserName(context: Context, name: String) {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("user_name", name).apply()
}

