package com.example.bluey

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import java.awt.Toolkit
import kotlin.math.min

private val Ink = Color(0xFF15111D)
private val Panel = Color(0xFF241C2D)
private val Berry = Color(0xFF8D4AA8)
private val Pink = Color(0xFFF18DB2)
private val Muted = Color(0xFFB7AABD)
private val Green = Color(0xFF8CD7A1)

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Bluey — Focus Friend") {
        MaterialTheme(colorScheme = darkColorScheme(
            background = Ink, surface = Panel, primary = Berry,
            secondary = Pink, onBackground = Color.White, onSurface = Color.White
        )) {
            BlueyDesktop()
        }
    }
}

@Composable
private fun BlueyDesktop() {
    var task by remember { mutableStateOf("A little focused work") }
    var minutes by remember { mutableIntStateOf(25) }
    var remaining by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }
    var completed by remember { mutableIntStateOf(0) }
    var status by remember { mutableStateOf("Ready when you are.") }

    LaunchedEffect(running) {
        while (running && remaining > 0) {
            delay(1000)
            remaining -= 1
            if (remaining == 0) {
                running = false
                completed += 1
                status = "Lovely work. Focus session complete!"
                chirp()
            }
        }
    }

    Surface(Modifier.fillMaxSize(), color = Ink) {
        Row(Modifier.fillMaxSize().padding(28.dp), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
            Column(
                Modifier.weight(1.15f).fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("YOUR FOCUS COMPANION", color = Pink, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Spacer(Modifier.height(18.dp))
                BlueberryFace(Modifier.size(340.dp))
                Spacer(Modifier.height(18.dp))
                Text("Bluey", fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Text(status, color = Muted, fontSize = 14.sp)
            }

            Card(
                Modifier.weight(0.85f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(26.dp)
            ) {
                Column(
                    Modifier.fillMaxSize().padding(26.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Focus session", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Text("What are we working on?", color = Muted, fontSize = 13.sp)
                    BasicTextField(
                        value = task,
                        onValueChange = { task = it },
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                            .background(Ink).padding(14.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Duration", color = Muted)
                        Spacer(Modifier.weight(1f))
                        Text("$minutes min", color = Pink, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = minutes.toFloat(),
                        onValueChange = { minutes = it.toInt().coerceIn(1, 120) },
                        valueRange = 1f..120f,
                        enabled = !running && remaining == 0
                    )
                    Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Text(formatTime(remaining), fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Pink)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                if (remaining == 0) {
                                    remaining = minutes * 60
                                    status = "Eyes on the prize. You've got this!"
                                }
                                running = true
                                chirp()
                            },
                            enabled = !running,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Berry)
                        ) { Text(if (remaining > 0) "Resume" else "Start focus") }

                        OutlinedButton(
                            onClick = {
                                running = false
                                if (remaining > 0) status = "Paused. Take your time."
                            },
                            enabled = running,
                            modifier = Modifier.weight(1f)
                        ) { Text("Pause") }
                    }
                    OutlinedButton(
                        onClick = {
                            running = false
                            remaining = 0
                            status = "Session reset. Ready for another?"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("End session") }

                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(9.dp).clip(CircleShape).background(Green))
                        Spacer(Modifier.width(8.dp))
                        Text("Focus sessions completed: $completed", color = Muted, fontSize = 13.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        "Bluey keeps your timer company. This Windows edition does not inspect your screen or claim to detect distractions.",
                        color = Muted, fontSize = 11.sp, lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BlueberryFace(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val breathe by transition.animateFloat(
        initialValue = 0.985f, targetValue = 1.015f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse)
    )
    var gaze by remember { mutableStateOf(Offset.Zero) }
    var blink by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3200)
            blink = true
            delay(130)
            blink = false
        }
    }

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    event.changes.firstOrNull()?.let {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        gaze = Offset(
                            ((it.position.x - center.x) / (size.width * 0.25f)).coerceIn(-1f, 1f),
                            ((it.position.y - center.y) / (size.height * 0.25f)).coerceIn(-1f, 1f)
                        )
                    }
                }
            }
        }
    ) {
        val s = min(size.width, size.height)
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = s * 0.43f * breathe
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFC78ADD), Color(0xFF77409D), Color(0xFF402253)),
                center = Offset(center.x - radius * .35f, center.y - radius * .45f),
                radius = radius * 1.7f
            ),
            radius = radius,
            center = center
        )
        drawCircle(Color.White.copy(alpha = .15f), radius * .16f, Offset(center.x - radius * .38f, center.y - radius * .58f))
        val eyeY = center.y - radius * .08f
        val eyeOffset = radius * .34f
        for (side in listOf(-1f, 1f)) {
            val eye = Offset(center.x + side * eyeOffset, eyeY)
            drawCircle(Color(0xFFFFF8F2), radius * .205f, eye)
            if (!blink) {
                val pupil = Offset(eye.x + gaze.x * radius * .075f, eye.y + gaze.y * radius * .075f)
                drawCircle(Color(0xFF24182D), radius * .105f, pupil)
                drawCircle(Color.White, radius * .035f, Offset(pupil.x - radius * .035f, pupil.y - radius * .04f))
            } else {
                drawLine(Color(0xFF24182D), Offset(eye.x - radius * .13f, eye.y), Offset(eye.x + radius * .13f, eye.y), radius * .025f, cap = StrokeCap.Round)
            }
        }
        drawCircle(Color(0xFFFFA4BD).copy(alpha = .65f), radius * .075f, Offset(center.x - radius * .57f, center.y + radius * .23f))
        drawCircle(Color(0xFFFFA4BD).copy(alpha = .65f), radius * .075f, Offset(center.x + radius * .57f, center.y + radius * .23f))
        drawArc(
            color = Color(0xFF321B38),
            startAngle = 15f, sweepAngle = 150f, useCenter = false,
            topLeft = Offset(center.x - radius * .19f, center.y + radius * .12f),
            size = androidx.compose.ui.geometry.Size(radius * .38f, radius * .25f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * .035f, cap = StrokeCap.Round)
        )
    }
}

private fun formatTime(seconds: Int): String =
    "%02d:%02d".format(seconds / 60, seconds % 60)

private fun chirp() {
    runCatching { Toolkit.getDefaultToolkit().beep() }
}
