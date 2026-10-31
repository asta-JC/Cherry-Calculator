package com.asta.calculatorapp

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.foundation.isSystemInDarkTheme
import com.asta.calculatorapp.ui.theme.Appearance
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.asta.calculatorapp.ui.screens.CalculatorScreen
import com.asta.calculatorapp.ui.theme.CalculatorAppTheme
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferences = getSharedPreferences("appearance", MODE_PRIVATE)
        val savedAppearance = runCatching {
            Appearance.valueOf(preferences.getString("mode", "SYSTEM") ?: "SYSTEM")
        }.getOrDefault(Appearance.SYSTEM)
        setContent {
            var appearance by remember { mutableStateOf(savedAppearance) }
            val dark = when (appearance) {
                Appearance.SYSTEM -> isSystemInDarkTheme()
                Appearance.LIGHT -> false
                Appearance.DARK -> true
            }
            CalculatorAppTheme(darkTheme = dark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen(appearance = appearance, onAppearanceChange = {
                        appearance = it
                        preferences.edit().putString("mode", it.name).apply()
                    })
                }
            }
        }
    }
}
