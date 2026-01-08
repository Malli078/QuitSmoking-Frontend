package com.simats.quitsmoking.screens.progress

import android.content.*
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.*
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@Composable
fun MilestoneCelebrationScreen(
    navController: NavController,
    milestone: String = "7 Days Smoke-Free!",
    message: String = "You're doing amazing!"
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showConfetti by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(3000)
        showConfetti = false
    }

    val particles = remember {
        List(30) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat() * -1f,
                size = 6f + Random.nextFloat() * 10f,
                speed = 0.5f + Random.nextFloat() * 2f,
                color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7C3AED),
                        Color(0xFFEC4899),
                        Color(0xFFF97316)
                    )
                )
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (showConfetti) ConfettiCanvas(particles)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🏆 Trophy
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(58.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "🎉 Congratulations! 🎉",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                milestone,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                message,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))

            // 🌟 Achievement Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "New Achievement Unlocked",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Every milestone is a victory. Your determination is inspiring!",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // 🔘 Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val uri = withContext(Dispatchers.IO) {
                                generateCertificateUri(context, milestone, message)
                            }
                            val shareText =
                                "🎉 $milestone\n\n$message\n\nI'm on my journey to quit smoking and just hit this milestone! 💪"
                            if (uri != null) {
                                shareImageUriAndText(context, uri, shareText)
                            } else {
                                copyToClipboard(context, shareText)
                                shareTextOnly(context, shareText)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Share Achievement", color = Color.Black)
                }

                Button(
                    onClick = {
                        scope.launch {
                            val ok = withContext(Dispatchers.IO) {
                                generateAndSaveCertificate(context, milestone, message)
                            }
                            Toast.makeText(
                                context,
                                if (ok) "Certificate downloaded!" else "Unable to generate certificate",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Download Certificate", color = Color.White)
                }

                Button(
                    onClick = {
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("Continue Journey", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

/* ------------------- Confetti Canvas ------------------- */
private data class ConfettiParticle(
    var x: Float,
    var y: Float,
    val size: Float,
    val speed: Float,
    val color: Color
)

@Composable
private fun ConfettiCanvas(list: List<ConfettiParticle>) {
    val transition = rememberInfiniteTransition()
    val anim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        list.forEachIndexed { i, p ->
            val yPos = (p.y + anim * (1f + i * 0.02f)) % 1f
            drawCircle(
                color = p.color,
                radius = p.size,
                center = Offset(p.x * w, yPos * h)
            )
        }
    }
}

/* ------------------- Certificate Helpers ------------------- */

private fun createCertificateBitmap(milestone: String, message: String): Bitmap {
    val w = 1600
    val h = 1200
    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)

    val bgPaint = Paint().apply {
        shader = LinearGradient(
            0f, 0f, w.toFloat(), h.toFloat(),
            intArrayOf(Color(0xFFA855F7).toArgb(), Color(0xFFEC4899).toArgb(), Color(0xFFF97316).toArgb()),
            floatArrayOf(0f, 0.6f, 1f),
            Shader.TileMode.CLAMP
        )
        isAntiAlias = true
    }
    canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), bgPaint)

    val titlePaint = Paint().apply {
        color = Color.White.toArgb()
        textSize = 72f
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText("Certificate of Achievement", w / 2f, 150f, titlePaint)

    val emojiPaint = Paint().apply {
        textSize = 140f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText("🏆", w / 2f, 320f, emojiPaint)

    val milestonePaint = Paint().apply {
        color = Color.White.toArgb()
        textSize = 56f
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText(milestone, w / 2f, 460f, milestonePaint)

    val msgPaint = Paint().apply {
        color = Color.White.toArgb()
        textSize = 36f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText(message, w / 2f, 540f, msgPaint)

    val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    val datePaint = Paint().apply {
        color = Color.White.toArgb()
        textSize = 28f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText(sdf.format(Date()), w / 2f, 660f, datePaint)

    val footerPaint = Paint().apply {
        color = Color.White.toArgb()
        textSize = 26f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText("Keep up the great work on your quit smoking journey!", w / 2f, 760f, footerPaint)

    return bmp
}

private fun generateAndSaveCertificate(context: Context, milestone: String, message: String): Boolean {
    return try {
        val bmp = createCertificateBitmap(milestone, message)
        val name = "quit-certificate-${System.currentTimeMillis()}.png"
        saveBitmap(context, bmp, name)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

private fun generateCertificateUri(context: Context, milestone: String, message: String): Uri? {
    return try {
        val bmp = createCertificateBitmap(milestone, message)
        val name = "quit-share-${System.currentTimeMillis()}.png"
        saveBitmapReturnUri(context, bmp, name)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun saveBitmap(context: Context, bmp: Bitmap, displayName: String): Boolean {
    val resolver = context.contentResolver
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(collection, values) ?: return false
    var out: OutputStream? = null
    return try {
        out = resolver.openOutputStream(uri) ?: return false
        val ok = bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        ok
    } finally {
        out?.close()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear()
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        }
    }
}

private fun saveBitmapReturnUri(context: Context, bmp: Bitmap, displayName: String): Uri? {
    val resolver = context.contentResolver
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(collection, values) ?: return null
    var out: OutputStream? = null
    return try {
        out = resolver.openOutputStream(uri) ?: return null
        val ok = bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        if (!ok) return null
        uri
    } finally {
        out?.close()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear()
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        }
    }
}

private fun shareImageUriAndText(context: Context, uri: Uri, text: String) {
    try {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(sendIntent, "Share Achievement")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    } catch (e: Exception) {
        e.printStackTrace()
        shareTextOnly(context, text)
    }
}

private fun shareTextOnly(context: Context, text: String) {
    try {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val chooser = Intent.createChooser(sendIntent, "Share Achievement")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Unable to share. Please try again.", Toast.LENGTH_SHORT).show()
    }
}

private fun copyToClipboard(context: Context, text: String) {
    try {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("achievement", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Achievement copied to clipboard!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Unable to copy to clipboard.", Toast.LENGTH_SHORT).show()
    }
}
