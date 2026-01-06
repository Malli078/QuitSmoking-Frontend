package com.example.quitsmoking.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.unit.sp

data class NavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)

@Composable
fun HomeBottomNavigation(navController: NavController) {

    val items: List<NavItem> = listOf(
        NavItem("home", Icons.Default.Home, "Home"),
        NavItem("health_dashboard", Icons.Default.Favorite, "Health"),
        NavItem("streak_calendar", Icons.Default.CalendarToday, "Progress"),   // ✅ Your progress tab
        NavItem("ai_chat", Icons.AutoMirrored.Filled.Message, "AI Chat"),
        NavItem("profile", Icons.Default.Person, "Profile"),
    )

    val navBackStackEntryState = navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntryState.value?.destination?.route

    Surface(
        tonalElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item: NavItem ->
                val selected = currentRoute == item.route

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(top = 6.dp)
                ) {

                    if (selected) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(0xFFDCFCE7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = item.label, tint = Color(0xFF059669))
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.label, tint = Color.Gray)
                    }

                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) Color(0xFF059669) else Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
