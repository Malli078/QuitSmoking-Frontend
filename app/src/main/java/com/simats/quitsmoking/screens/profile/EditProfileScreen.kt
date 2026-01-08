package com.simats.quitsmoking.screens.profile

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

/* -------- Helper functions -------- */
private fun bitmapFromUri(context: Context, uri: android.net.Uri): Bitmap? =
    try {
        context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
    } catch (e: Exception) {
        null
    }

private fun bitmapToBase64(bitmap: Bitmap): String {
    val output = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
    return Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
}

private fun base64ToBitmap(encoded: String?): Bitmap? =
    try {
        if (encoded.isNullOrEmpty()) null else {
            val bytes = Base64.decode(encoded, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    } catch (e: Exception) {
        null
    }

/* -------- Main Composable -------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    var name by remember { mutableStateOf(prefs.getString("name", "") ?: "") }
    var email by remember { mutableStateOf(prefs.getString("email", "") ?: "") }
    var quitDate by remember { mutableStateOf(prefs.getString("quit_date", "") ?: "") }
    var avatarBitmap by remember { mutableStateOf(base64ToBitmap(prefs.getString("avatar_base64", null))) }

    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    var showChooser by remember { mutableStateOf(false) }

    /* -------- Gallery & Camera Launchers -------- */
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                bitmapFromUri(context, it)?.let { bmp ->
                    avatarBitmap = bmp
                    prefs.edit().putString("avatar_base64", bitmapToBase64(bmp)).apply()
                }
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
            bmp?.let {
                avatarBitmap = it
                prefs.edit().putString("avatar_base64", bitmapToBase64(it)).apply()
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) cameraLauncher.launch(null)
            else Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }

    /* -------- UI -------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6F8))
    ) {

        /* ---------- Header ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
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
                    "Edit Profile",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 22.sp
                )
                Text(
                    "Update your personal information",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        /* ---------- Profile Avatar (moved lower) ---------- */
        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF10B981)),
                contentAlignment = Alignment.Center
            ) {
                if (avatarBitmap != null) {
                    Image(
                        bitmap = avatarBitmap!!.asImageBitmap(),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = name.firstOrNull()?.uppercase() ?: "U",
                        color = Color.White,
                        fontSize = 36.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .align(Alignment.BottomEnd)
                    .clickable { showChooser = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Photo",
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- Content Card ---------- */
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(20.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color(0xFF10B981),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.Black) },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color(0xFF10B981),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = quitDate,
                onValueChange = {},
                enabled = false,
                label = { Text("Quit Date (Future only)", color = Color.Black) },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = Color.Black) },
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
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color(0xFF10B981),
                    unfocusedBorderColor = Color.Gray,
                    disabledTextColor = Color.Black,
                    disabledLabelColor = Color.Black
                )
            )

            Spacer(Modifier.height(6.dp))
            Text(
                "Quit date must be in the future",
                color = Color.Black.copy(alpha = 0.7f),
                fontSize = 13.sp
            )

            Spacer(Modifier.weight(1f))

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
                colors = ButtonDefaults.buttonColors(Color(0xFF10B981))
            ) {
                Text("Save Changes", color = Color.White)
            }
        }
    }

    /* ---------- Image Picker Dialog ---------- */
    if (showChooser) {
        AlertDialog(
            onDismissRequest = { showChooser = false },
            title = { Text("Change Profile Photo", color = Color.Black) },
            confirmButton = {
                TextButton(onClick = {
                    showChooser = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Choose from Gallery", color = Color(0xFF059669))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showChooser = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Use Camera", color = Color(0xFF059669))
                }
            }
        )
    }
}
