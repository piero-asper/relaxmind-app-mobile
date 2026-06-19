package com.relaxmind.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.relaxmind.app.ui.themes.RelaxMindTheme

/**
 * A simple centered circular progress indicator.
 * Wrap in a Box or layout to position it as needed.
 */
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * A reusable full-screen solid loading screen with a message.
 * Completely replaces the content underneath (e.g. used during transitions or setup saving).
 */
@Composable
fun FullScreenLoadingScreen(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = indicatorColor)
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

/**
 * A reusable full-screen semi-transparent overlay that displays a centered loading indicator.
 * Sits on top of the existing layout to block interactions while showing progress.
 */
@Composable
fun FullScreenLoadingOverlay(
    modifier: Modifier = Modifier,
    overlayColor: Color = Color.Black.copy(alpha = 0.25f)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(overlayColor),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(modifier)
    }
}

@Preview(name = "LoadingIndicator Light", showBackground = true)
@Composable
private fun LoadingIndicatorLightPreview() {
    RelaxMindTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
        }
    }
}

@Preview(name = "FullScreenLoadingScreen Light", showBackground = true)
@Composable
private fun FullScreenLoadingScreenPreview() {
    RelaxMindTheme(darkTheme = false) {
        FullScreenLoadingScreen(text = "Guardando tus preferencias...")
    }
}

@Preview(name = "FullScreenLoadingOverlay Light", showBackground = true)
@Composable
private fun FullScreenLoadingOverlayPreview() {
    RelaxMindTheme(darkTheme = false) {
        FullScreenLoadingOverlay()
    }
}

