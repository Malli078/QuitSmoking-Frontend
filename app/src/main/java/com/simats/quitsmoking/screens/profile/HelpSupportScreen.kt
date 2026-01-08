@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.simats.quitsmoking.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simats.quitsmoking.viewmodel.HelpSupportViewModel

@Composable
fun HelpSupportScreen(
    navController: NavController,
    viewModel: HelpSupportViewModel = viewModel()
) {
    val topics by viewModel.topics.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHelpData()
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
                    text = "Help & Support",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 22.sp
                )
                Text(
                    text = "We’re here to assist you",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        /* ---------- Main Content ---------- */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (loading) {
                CircularProgressIndicator(color = Color(0xFF059669))
                return@Column
            }

            error?.let {
                Text(it, color = Color.Red, fontWeight = FontWeight.Bold)
                return@Column
            }

            // 📘 FAQ Section
            Text(
                "Frequently Asked Questions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            topics.forEach { topic ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Q: ${topic.title}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "A: ${topic.description}",
                            fontSize = 14.sp,
                            color = Color(0xFF374151),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // 📞 Contact Section
            Text(
                "Contact Us",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(12.dp))

            ContactRow(Icons.Default.Email, "Email", "malliyaram427@gmail.com")
            Spacer(Modifier.height(12.dp))
            ContactRow(Icons.Default.Phone, "Phone", "9059804594")

            Spacer(Modifier.height(40.dp))
        }
    }
}

/* ---------- Contact Row ---------- */
@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                when (title) {
                    "Email" -> {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$value"))
                        context.startActivity(intent)
                    }

                    "Phone" -> {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$value"))
                        context.startActivity(intent)
                    }
                }
            },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD1FAE5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color(0xFF047857))
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Text(
                    title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    value,
                    fontSize = 14.sp,
                    color = Color(0xFF1F2937)
                )
            }
        }
    }
}
