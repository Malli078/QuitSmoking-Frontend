package com.example.quitsmoking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.material3.Surface
import com.example.quitsmoking.network.RetrofitClient
import com.example.quitsmoking.network.ApiService
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quitsmoking.screens.health.AIHealthPredictionScreen
import com.example.quitsmoking.screens.health.BiometricsEntryScreen
import com.example.quitsmoking.screens.health.HealthTimelineScreen
import com.example.quitsmoking.screens.health.LungRecoveryScreen
import com.example.quitsmoking.screens.health.HeartRecoveryScreen
import com.example.quitsmoking.screens.health.TasteSmellRecoveryScreen
import com.example.quitsmoking.screens.health.EnergyImprovementScreen
import com.example.quitsmoking.screens.health.SymptomsBetterScreen
import com.example.quitsmoking.ui.theme.QuitSmokingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuitSmokingAppTheme {

                val apiService: ApiService = remember {
                    RetrofitClient.api

                }

                val userId = remember { 3 }

                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        // ---------------- APP FLOW ----------------
                        composable("splash") {
                            com.example.quitsmoking.screens.SplashScreen(navController)
                        }
                        composable("welcome") {
                            com.example.quitsmoking.screens.WelcomeScreen(navController)
                        }
                        // ---------------- AUTH --------------------
                        composable("login") {
                            com.example.quitsmoking.screens.LoginScreen(navController)
                        }
                        composable("forgot_password") {
                            com.example.quitsmoking.screens.ForgotPasswordScreen(navController)
                        }
                        // ---------- RESET PASSWORD (ADD THIS ONLY) ----------
                        composable(
                            route = "reset_password/{email}",
                            arguments = listOf(
                                androidx.navigation.navArgument("email") {
                                    type = androidx.navigation.NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            com.example.quitsmoking.screens.ResetPasswordScreen(
                                navController = navController,
                                email = email
                            )
                        }

                        composable("create_account") {
                            com.example.quitsmoking.screens.CreateAccountScreen(navController)
                        }
                        // ------------- ONBOARDING ------------------
                        composable("why_quit") {
                            com.example.quitsmoking.screens.WhyQuitScreen(navController)
                        }
                        composable("smoking_habit") {
                            com.example.quitsmoking.screens.SmokingHabitScreen(navController)
                        }
                        composable("triggers") {
                            com.example.quitsmoking.screens.TriggerIdentificationScreen(navController)
                        }
                        // ---------------- HOME ---------------------
                        composable("home") {
                            com.example.quitsmoking.screens.home.HomeScreen(navController)
                        }
                        composable("daily_motivation") {
                            com.example.quitsmoking.screens.home.DailyMotivationScreen(navController)
                        }
                        composable("craving_prediction") {
                            com.example.quitsmoking.screens.home.CravingPredictionScreen(navController)
                        }
                        composable("daily_progress") {
                            com.example.quitsmoking.screens.home.DailyProgressScreen(navController)
                        }
                        // ---------- CRAVING FLOW ----------
                        composable("craving-alert") {
                            com.example.quitsmoking.screens.home.CravingAlertScreen(navController)
                        }
                        composable("craving_severity") {
                            com.example.quitsmoking.screens.home.CravingSeverityScreen(navController)
                        }
                        composable("craving_type") {
                            com.example.quitsmoking.screens.home.CravingTypeScreen(navController)
                        }
                        composable("craving-type") {
                            com.example.quitsmoking.screens.home.CravingTypeScreen(navController)
                        }
                        composable("craving-reason") {
                            com.example.quitsmoking.screens.home.CravingReasonScreen(navController)
                        }
                        composable("craving_reason") {
                            com.example.quitsmoking.screens.home.CravingReasonScreen(navController)
                        }
                        composable("craving-timer") {
                            com.example.quitsmoking.screens.home.CravingTimerScreen(navController)
                        }
                        composable("craving_timer") {
                            com.example.quitsmoking.screens.home.CravingTimerScreen(navController)
                        }
                        composable("craving-success") {
                            com.example.quitsmoking.screens.home.CravingSuccessScreen(navController)
                        }
                        composable("craving_success") {
                            com.example.quitsmoking.screens.home.CravingSuccessScreen(navController)
                        }
                        // ⭐ BREATHING EXERCISE
                        composable("breathing-exercise") {
                            com.example.quitsmoking.screens.home.BreathingExerciseScreen(navController)
                        }
                        composable("breathing_exercise") {
                            com.example.quitsmoking.screens.home.BreathingExerciseScreen(navController)
                        }
                        // ---------- QUICK ACTIONS ----------
                        composable("quick_actions") {
                            com.example.quitsmoking.screens.home.QuickActionsScreen(navController)
                        }
                        // ---------- TODAY GOALS ----------
                        composable("todays_goals") {
                            com.example.quitsmoking.screens.home.TodaysGoalScreen(navController)
                        }
                        // ---------- STREAK CALENDAR ----------
                        // ---------- STREAK CALENDAR ----------
                        composable("streak_calendar") {
                            com.example.quitsmoking.screens.progress.StreakCalendarScreen(
                                navController = navController
                            )
                        }

// ---------- MONEY SAVED ----------
                        composable("money_saved") {
                            com.example.quitsmoking.screens.progress.MoneySavedScreen(
                                navController = navController
                            )
                        }

// ---------- MILESTONE ----------
                        composable("milestone") {
                            com.example.quitsmoking.screens.progress.MilestoneCelebrationScreen(
                                navController = navController
                            )
                        }

// ---------- ACHIEVEMENTS ----------
                        composable("achievements") {
                            com.example.quitsmoking.screens.progress.AchievementsScreen(
                                navController = navController
                            )
                        }

                        // ---------- HELP TOPIC DETAIL ----------
                        composable(
                            route = "help_topic/{id}",
                            arguments = listOf(
                                androidx.navigation.navArgument("id") {
                                    type = androidx.navigation.NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")

                            // TEMP PLACEHOLDER (SAFE)
                            AppSimplePlaceholder(
                                text = "Help Topic ID: $id",
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // ---------- PROFILE ----------
                        composable("profile") {
                            com.example.quitsmoking.screens.profile.ProfileScreen(navController)
                        }
                        composable("edit_profile") {
                            com.example.quitsmoking.screens.profile.EditProfileScreen(navController)
                        }
                        composable("habit_settings") {
                            com.example.quitsmoking.screens.profile.HabitSettingsScreen(navController)
                        }
                        composable("quit_plan_settings") {
                            com.example.quitsmoking.screens.profile.QuitPlanSettingsScreen(navController)
                        }
                        composable("notification_settings") {
                            com.example.quitsmoking.screens.profile.NotificationSettingsScreen(navController)
                        }
                        composable("app_settings") {
                            com.example.quitsmoking.screens.profile.AppSettingsScreen(navController)
                        }
                        composable("privacy") {
                            com.example.quitsmoking.screens.profile.PrivacySecurityScreen(navController)
                        }
                        composable("help_support") {
                            com.example.quitsmoking.screens.profile.HelpSupportScreen(navController)
                        }
                        // ---------- AI ----------
                        composable("ai_chat") {
                            com.example.quitsmoking.screens.ai.AIChatScreen(
                                onBack = { navController.navigateUp() },
                                apiService = apiService
                            )
                        }

                        composable("ai_notification_settings") {
                            com.example.quitsmoking.screens.ai.NotificationScreen(navController)
                        }
                        composable("notifications") {
                            com.example.quitsmoking.screens.ai.NotificationsScreen(navController)
                        }

                        composable("personalized_tips") {
                            com.example.quitsmoking.screens.ai.PersonalizedTipsScreen(navController)
                        }
                        composable("stress_suggestions") {
                            com.example.quitsmoking.screens.ai.StressSuggestionsScreen(navController)
                        }
                        composable("trigger_alert") {
                            com.example.quitsmoking.screens.ai.TriggerAlertScreen(navController)
                        }
                        // ---------- HEALTH DASHBOARD ----------
                        composable("health_dashboard") {
                            com.example.quitsmoking.screens.health.HealthDashboardScreen(
                                navController = navController,
                                user = null
                            )
                        }
                        // ---------- HEALTH TIMELINE ----------
                        composable("health_timeline") {
                            HealthTimelineScreen(
                                navController = navController,
                                quitDate = null
                            )
                        }
                        // ---------- RECOVERY GRAPH ----------
                        composable("recovery-graph") {
                            com.example.quitsmoking.screens.health.RecoveryGraphScreen(
                                navController = navController,
                                quitDateIso = null
                            )
                        }
                        // ---------- SYMPTOMS BETTER ----------
                        composable("symptoms-better") {
                            SymptomsBetterScreen(navController = navController)
                        }
                        // ---------- AI HEALTH PREDICTION ----------
                        composable("ai-health-prediction") {
                            // pass user if you have it; for now we pass null
                            AIHealthPredictionScreen(navController = navController, user = null)
                        }
                        // ---------- LUNG RECOVERY ----------
                        composable("lung-recovery") {
                            LungRecoveryScreen(navController = navController, quitDateMillis = null)
                        }
                        // ---------- HEART RECOVERY ----------
                        composable("heart-recovery") {
                            HeartRecoveryScreen(navController = navController, quitDateMillis = null)
                        }
                        // ---------- TASTE & SMELL RECOVERY ----------
                        // This is the route your HealthDashboardScreen uses: "taste-smell-recovery"
                        composable("taste-smell-recovery") {
                            TasteSmellRecoveryScreen(navController = navController, quitDateMillis = null)
                        }
                        // ---------- ENERGY IMPROVEMENT ----------
                        // <-- ADDED: navigation entry so clicking the "Energy Levels" metric opens the screen
                        composable("energy_improvement") {
                            EnergyImprovementScreen(navController = navController, quitDateMillis = null)
                        }
                        // ---------- BIOMETRICS ENTRY ----------
                        composable("biometrics_entry") {
                            BiometricsEntryScreen(
                                navController = navController,
                                addHealthMetrics = { metrics ->
                                    // TODO: persist metrics (ViewModel/Room) — currently navigate back
                                    navController.navigate("health_dashboard") {
                                        popUpTo("health_dashboard") { inclusive = false }
                                    }
                                }
                            )
                        }
                    } // NavHost
                } // Surface
            } // QuitSmokingAppTheme
        } // setContent
    } // onCreate
} // MainActivity

// ---------------------------------------------------------------
// Placeholder (unchanged)
@Composable
fun AppSimplePlaceholder(text: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBack) { Text("Back") }
        }
    }
}
