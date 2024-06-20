package com.a9992099300.notes

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import data.database.getDatabaseBuilder
import di.PlatformConfiguration
import di.PlatformSDK

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = getDatabaseBuilder(applicationContext)
        PlatformSDK.init(
            PlatformConfiguration(activityContext = applicationContext,
            appName = "APP_NAME"),
            database = appDatabase
        )

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}