package com.example.multisensorapprakha

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var accelSensor: Sensor? = null

    // State Reactive untuk Jetpack Compose
    private var lightLux by mutableStateOf(100f)
    private var accelX by mutableStateOf(0f)
    private var accelY by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi SensorManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Sensor 1: Category Environment (Light)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        // Sensor 2: Category Motion (Accelerometer)
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            // Efek UI 1: Perubahan warna background sesuai tingkat cahaya sekitar
            val isDarkMode = lightLux < 40f
            val backgroundColor = if (isDarkMode) Color(0xFF1E1E2E) else Color(0xFFF0F4F8)
            val textColor = if (isDarkMode) Color.White else Color(0xFF1E1E2E)
            val cardBg = if (isDarkMode) Color(0xFF2B2B3D) else Color.White

            // Smooth animasi gerak bola
            val animatedX by animateFloatAsState(targetValue = -accelX * 15f, label = "X")
            val animatedY by animateFloatAsState(targetValue = accelY * 15f, label = "Y")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Multi-Sensor Dashboard",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    // Card Info Sensor Cahaya (Environment)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "1. Sensor Cahaya (Environment)",
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Intensitas: ${lightLux.toInt()} Lux", color = textColor)
                            Text(
                                text = "Status Mode: ${if (isDarkMode) "Dark Mode (Gelap)" else "Light Mode (Terang)"}",
                                color = textColor,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Card Info Accelerometer (Motion)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "2. Accelerometer (Motion)",
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "X: ${String.format("%.2f", accelX)} | Y: ${String.format("%.2f", accelY)}",
                                color = textColor
                            )
                        }
                    }

                    // Area Efek UI 2: Bola bergerak sesuai kemiringan HP
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(cardBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .offset(x = animatedX.dp, y = animatedY.dp)
                                .size(40.dp)
                                .background(Color(0xFF6C5CE7), CircleShape)
                        )
                    }
                }
            }
        }
    }

    // --- Lifecycle Sensor Wajib (register / unregister) ---
    override fun onResume() {
        super.onResume()
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        accelSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_LIGHT -> {
                    lightLux = it.values[0]
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    accelX = it.values[0]
                    accelY = it.values[1]
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}