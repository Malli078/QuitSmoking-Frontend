package com.example.quitsmoking.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
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
import com.example.quitsmoking.viewmodel.ResetPasswordViewModel

@Composable
fun ResetPasswordScreen(navController: NavController, email: String) {
    val context = LocalContext.current
    val vm: ResetPasswordViewModel = viewModel()
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ✅ After password reset → Go to login
    LaunchedEffect(vm.success.value) {
        if (vm.success.value) {
            navController.navigate("login") {
                popUpTo("reset_password") { inclusive = true }
            }
        }
    }

    // 🌫️ Gentle smoke animation
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
        // 🖼️ Background smoke
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://as2.ftcdn.net/v2/jpg/10/51/32/77/1000_F_1051327760_G7G4vuZxAwYcBDEcbHL5PGy9nRDaH8Pw.jpg")
                .crossfade(true)
                .build(),
            contentDescription = "Smoke Background",
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
                .alpha(0.6f)
                .offset(y = offsetY.dp),
            contentScale = ContentScale.Crop
        )

        // 💨 Frosted Glass Box
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
                // 🚭 Circular Logo
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

                // 🧾 Title
                Text(
                    text = "Reset Password",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(24.dp))

                // 🔢 OTP Field
                OutlinedTextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("OTP", color = Color.Black) },
                    leadingIcon = {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color.Black)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )

                Spacer(Modifier.height(12.dp))

                // 🔒 New Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("New Password", color = Color.Black) },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )

                if (vm.error.value.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(vm.error.value, color = Color.Red)
                }

                Spacer(Modifier.height(20.dp))

                // 🔘 Reset Password Button
                Button(
                    onClick = {
                        if (otp.isNotBlank() && password.isNotBlank()) {
                            vm.reset(email, otp, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !vm.loading.value
                ) {
                    if (vm.loading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Reset Password", color = Color.White)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // 🔙 Back to Login
                Text(
                    text = "Back to Login",
                    color = Color(0xFF00695C),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("login") {
                            popUpTo("reset_password") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
