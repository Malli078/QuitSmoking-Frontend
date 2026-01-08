package com.simats.quitsmoking.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class CravingType(
    val id: String,
    val label: String,
    val description: String,
    val iconType: IconType,
    val bgColor: Color,
    val iconTint: Color
)

private enum class IconType { Zap, Clock, Users, Frown, Coffee, Moon }

@Composable
fun CravingTypeScreen(navController: NavController) {

    var selectedType by rememberSaveable { mutableStateOf<String?>(null) }

    val types = listOf(
        CravingType("physical", "Physical", "Body wants nicotine", IconType.Zap, Color(0xFFFFF7ED), Color(0xFFEA580C)),
        CravingType("habitual", "Habitual", "Routine trigger", IconType.Clock, Color(0xFFEEF2FF), Color(0xFF1E40AF)),
        CravingType("social", "Social", "Around smokers", IconType.Users, Color(0xFFF5F3FF), Color(0xFF6D28D9)),
        CravingType("emotional", "Emotional", "Stress or mood", IconType.Frown, Color(0xFFFFEEF0), Color(0xFFDC2626)),
        CravingType("situational", "Situational", "Specific activity", IconType.Coffee, Color(0xFFFFFBEB), Color(0xFFB45309)),
        CravingType("other", "Other", "Something else", IconType.Moon, Color(0xFFF3F4F6), Color(0xFF374151))
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            /* ---------- HEADER ---------- */
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }   // ✅ FIXED
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

            /* ---------- CONTENT ---------- */
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {

                Text(
                    text = "What type of craving?",
                    fontSize = 22.sp,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "This helps us provide the right support",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(18.dp))

                val chunks = types.chunked(2)
                chunks.forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        pair.forEach { type ->
                            val isSelected = selectedType == type.id

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(if (isSelected) Color(0xFFFFF1F1) else Color.White)
                                    .clickable { selectedType = type.id }
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(type.bgColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (type.iconType) {
                                        IconType.Zap -> Icon(Icons.Default.FlashOn, null, tint = type.iconTint)
                                        IconType.Clock -> Icon(Icons.Default.AccessTime, null, tint = type.iconTint)
                                        IconType.Users -> Icon(Icons.Default.People, null, tint = type.iconTint)
                                        IconType.Frown -> Icon(Icons.Default.SentimentDissatisfied, null, tint = type.iconTint)
                                        IconType.Coffee -> Icon(Icons.Default.LocalCafe, null, tint = type.iconTint)
                                        IconType.Moon -> Icon(Icons.Default.Bedtime, null, tint = type.iconTint)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(type.label, color = Color(0xFF111827))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(type.description, fontSize = 12.sp, color = Color(0xFF6B7280))
                            }
                        }

                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            /* ---------- FOOTER ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val enabled = selectedType != null

                Button(
                    onClick = {
                        if (enabled) {
                            navController.navigate("craving-reason")
                        }
                    },
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (enabled) Color(0xFFDC2626) else Color(0xFFF3F4F6),
                        contentColor = if (enabled) Color.White else Color(0xFF9CA3AF)
                    )
                ) {
                    Text("Continue")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}
