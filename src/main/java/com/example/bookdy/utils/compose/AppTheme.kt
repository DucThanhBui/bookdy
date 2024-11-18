package com.example.bookdy.utils.compose

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Setup the Compose app-wide theme.
 */
@Composable
fun AppTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            when {
                useDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
                else -> dynamicLightColorScheme(LocalContext.current)
            }
        }
        useDarkTheme -> lightColorScheme()
        else -> darkColorScheme()
    }
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
