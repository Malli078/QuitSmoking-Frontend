package com.simats.quitsmoking.screens.ai

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.quitsmoking.network.ApiService
import com.simats.quitsmoking.network.AiChatRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class Message(
    val id: String,
    val text: String,
    val sender: Sender,
    val timestamp: Date = Date()
)

enum class Sender { USER, AI }

@SuppressLint("SimpleDateFormat")
@Composable
fun AIChatScreen(
    onBack: () -> Unit,
    apiService: ApiService
) {
    val dateFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val messages = remember {
        mutableStateListOf(
            Message(
                id = "1",
                text = "Hi! I'm your AI quit coach. I'm here to help you anytime.",
                sender = Sender.AI
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun sendMessage(text: String) {
        if (text.isBlank() || isLoading) return

        val userMessage = Message(
            id = UUID.randomUUID().toString(),
            text = text.trim(),
            sender = Sender.USER
        )

        messages.add(userMessage)
        inputText = ""
        focusManager.clearFocus()

        scope.launch {
            isLoading = true
            try {
                val response = apiService.askAi(AiChatRequest(question = userMessage.text))
                val body = response.body()

                messages.add(
                    Message(
                        id = UUID.randomUUID().toString(),
                        text = if (response.isSuccessful && body?.success == true)
                            body.reply
                        else
                            "AI did not respond. Please try again.",
                        sender = Sender.AI
                    )
                )
            } catch (e: Exception) {
                messages.add(
                    Message(
                        id = UUID.randomUUID().toString(),
                        text = "Unable to connect. Please try again.",
                        sender = Sender.AI
                    )
                )
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            val gradient = Brush.linearGradient(
                listOf(Color(0xFF3B82F6), Color(0xFF7C3AED))
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp)
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "AI Support",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.sender == Sender.USER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isUser) Color(0xFF059669) else Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                msg.text,
                                modifier = Modifier.padding(12.dp),
                                color = if (isUser) Color.White else Color.Black
                            )
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Text(
                            "AI is typing...",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your message") },
                    singleLine = true
                )
                Spacer(Modifier.width(12.dp))
                IconButton(
                    onClick = { sendMessage(inputText) },
                    enabled = !isLoading
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null)
                }
            }
        }
    }
}
