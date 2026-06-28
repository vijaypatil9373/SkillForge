package com.example.skillforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.skillforge.navigation.AppNavGraph
import com.example.skillforge.ui.theme.SkillForgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SkillForgeTheme {
                AppNavGraph()
            }
        }
    }
}