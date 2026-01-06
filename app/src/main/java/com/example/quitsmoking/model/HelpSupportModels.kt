package com.example.quitsmoking.model

// ---------- HELP DATA ----------

data class HelpTopic(
    val id: Int,
    val title: String,
    val description: String
)

data class ContactInfo(
    val email: String,
    val phone: String
)

data class GetHelpResponse(
    val status: Boolean,
    val topics: List<HelpTopic>,
    val contact: ContactInfo
)

// ---------- SUPPORT TICKET ----------

data class SupportTicketRequest(
    val user_id: Int,
    val subject: String,
    val message: String
)

data class SupportTicketResponse(
    val status: Boolean,
    val message: String
)
