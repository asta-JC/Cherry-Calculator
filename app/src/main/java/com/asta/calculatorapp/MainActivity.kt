package com.asta.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.asta.calculatorapp.ui.screens.CalculatorScreen
import com.asta.calculatorapp.ui.theme.CalculatorAppTheme
import com.asta.calculatorapp.ui.theme.DarkBgPrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBgPrimary
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}
