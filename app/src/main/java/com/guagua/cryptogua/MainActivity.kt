package com.guagua.cryptogua

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.guagua.cryptogua.ui.navigation.AppNavHost
import com.guagua.cryptogua.ui.theme.CryptoGuaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoGuaTheme {
                val navController = rememberNavController()
                Scaffold { _ ->
                    AppNavHost(
                        modifier = Modifier.Companion.fillMaxSize(),
                        navController = navController
                    )
                }
            }
        }
    }
}