package com.example.quitsmoking.screens.ai

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
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
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    // messages state
    val messages = remember {
        mutableStateListOf(
            Message(
                id = "1",
                text = "Hi! I'm your AI quit coach. I'm here 24/7 to support you. How are you feeling right now?",
                sender = Sender.AI,
                timestamp = Date()
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val aiResponses = listOf(
        "That's completely normal! Cravings typically pass within 3-5 minutes. Try the breathing exercise or distract yourself with a quick walk.",
        "I'm proud of you for reaching out instead of giving in! Let's get through this together. What triggered this craving?",
        "Remember why you started this journey. Your body is already healing. Would you like to see your health progress?",
        "You're doing great! Every craving you resist makes you stronger. What can I help you with right now?",
        "That's a common challenge. Many people feel the same way. Have you tried the 5-minute distraction technique?"
    )

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return

        val userMessage = Message(
            id = System.currentTimeMillis().toString(),
            text = trimmed,
            sender = Sender.USER,
            timestamp = Date()
        )

        messages.add(userMessage)
        inputText = ""
        focusManager.clearFocus()

        coroutineScope.launch {
            delay(1000L)
            val aiText = aiResponses.random()
            val aiMessage = Message(
                id = (System.currentTimeMillis() + 1).toString(),
                text = aiText,
                sender = Sender.AI,
                timestamp = Date()
            )
            messages.add(aiMessage)
        }
    }

    Scaffold(
        topBar = {
            val gradient = Brush.linearGradient(
                colors = listOf(Color(0xFF3B82F6), Color(0xFF7C3AED))
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(brush = gradient),
                color = Color.Transparent,
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Android,
                                contentDescription = "Bot",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "AI Support", color = Color.White, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Always here to help", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Messages list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { message ->
                        val isUser = message.sender == Sender.USER
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {
                            if (!isUser) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFDBEAFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Android,
                                        contentDescription = "Bot",
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Column(
                                horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
                                modifier = Modifier.widthIn(max = 280.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(18.dp),
                                    color = if (isUser) Color(0xFF059669) else Color.White,
                                    tonalElevation = 1.dp,
                                    shadowElevation = 2.dp,
                                ) {
                                    Text(
                                        text = message.text,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isUser) Color.White else Color(0xFF111827)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = dateFormat.format(message.timestamp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

                            if (isUser) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFD1FAE5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "User",
                                        tint = Color(0xFF065F46),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Quick responses
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickChip("💪 Having a craving") {
                        inputText = "I'm having a strong craving"
                        sendMessage(inputText)
                    }
                    QuickChip("📊 Show progress") {
                        inputText = "Show my progress"
                        sendMessage(inputText)
                    }
                    QuickChip("⭐ Motivate me") {
                        inputText = "Give me motivation"
                        sendMessage(inputText)
                    }
                }

                // Input area
                Surface(
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Type your message...") },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(28.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) sendMessage(inputText)
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2563EB)),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun QuickChip(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .height(36.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 1.dp,
        color = Color.White
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color(0xFF374151))
        }
    }
}
