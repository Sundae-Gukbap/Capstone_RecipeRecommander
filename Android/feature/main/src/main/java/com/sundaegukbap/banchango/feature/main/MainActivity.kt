package com.sundaegukbap.banchango.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sundaegukbap.banchango.core.designsystem.theme.BanchangoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var systemUiController: SystemUiController

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            BanchangoTheme {
                val navController = rememberMainNavigator()
                systemUiController = rememberSystemUiController()
                systemUiController.setNavigationBarColor(color = Color(0xFFFFFFFF))

                MainScreen(
                    navigator = navController,
                    onChangeDarkTheme = {},
                    onChangeStatusBarColor = { color, darkIcons ->
                        systemUiController.setStatusBarColor(color = color, darkIcons = darkIcons,)
                    },
                )
            }
        }
    }
}
