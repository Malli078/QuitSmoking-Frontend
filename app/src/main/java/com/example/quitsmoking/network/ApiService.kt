package com.example.quitsmoking.network

import com.example.quitsmoking.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    /* ================= AUTH ================= */

    // 🔑 LOGIN
    @POST("login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // 🆕 REGISTER
    @POST("register.php")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    // 🔐 FORGOT PASSWORD
    @POST("send_otp.php")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<CommonResponse>


    // 🔁 RESET PASSWORD
    @POST("reset_password.php")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<CommonResponse>


    /* ================= PROFILE ================= */

    // 👤 GET PROFILE
    @GET("get_profile.php")
    suspend fun getProfile(
        @Query("user_id") userId: Int
    ): Response<GetProfileResponse>

    // ✏️ UPDATE PROFILE
    @POST("update_profile.php")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<CommonResponse>


    /* ================= HABIT SETTINGS ================= */

    @GET("get_habit_settings.php")
    suspend fun getHabitSettings(
        @Query("user_id") userId: Int
    ): Response<GetHabitSettingsResponse>

    @POST("update_habit_settings.php")
    suspend fun updateHabitSettings(
        @Body request: UpdateHabitSettingsRequest
    ): Response<CommonResponse>


    /* ================= QUIT PLAN ================= */

    @GET("get_quit_plan.php")
    suspend fun getQuitPlan(
        @Query("user_id") userId: Int
    ): Response<GetQuitPlanResponse>

    @POST("update_quit_plan.php")
    suspend fun updateQuitPlan(
        @Body request: UpdateQuitPlanRequest
    ): Response<CommonResponse>


    /* ================= NOTIFICATION SETTINGS ================= */

    @GET("get_notification_settings.php")
    suspend fun getNotificationSettings(
        @Query("user_id") userId: Int
    ): Response<GetNotificationSettingsResponse>

    @POST("update_notification_settings.php")
    suspend fun updateNotificationSettings(
        @Body request: UpdateNotificationSettingsRequest
    ): Response<CommonResponse>


    /* ================= APP SETTINGS ================= */

    @GET("get_app_settings.php")
    suspend fun getAppSettings(
        @Query("user_id") userId: Int
    ): Response<GetAppSettingsResponse>

    @POST("update_app_settings.php")
    suspend fun updateAppSettings(
        @Body request: UpdateAppSettingsRequest
    ): Response<CommonResponse>


    /* ================= STREAK / PROGRESS ================= */

    // 📅 STREAK CALENDAR (JSON BODY)
    @POST("get_streak_calendar.php")
    suspend fun getStreakCalendar(
        @Body request: CalendarRequest
    ): CalendarResponse

    // 📊 STREAK STATS (JSON BODY)
    @POST("get_streak_stats.php")
    suspend fun getStreakStats(
        @Body request: StatsRequest
    ): StatsResponse


    /* ================= EXPORT / DELETE ================= */

    @GET("export_user_data.php")
    suspend fun exportUserData(
        @Query("user_id") userId: Int
    ): Response<CommonResponse>

    @POST("delete_account.php")
    suspend fun deleteAccount(
        @Body body: Map<String, Int>
    ): Response<CommonResponse>


    /* ================= HELP & SUPPORT ================= */

    // 🆘 GET HELP CONTENT
    @GET("get_help_support.php")
    suspend fun getHelpSupport(): Response<GetHelpResponse>

    // 📨 SUBMIT SUPPORT TICKET
    @POST("submit_support_ticket.php")
    suspend fun submitSupportTicket(
        @Body request: SupportTicketRequest
    ): Response<SupportTicketResponse>
}
