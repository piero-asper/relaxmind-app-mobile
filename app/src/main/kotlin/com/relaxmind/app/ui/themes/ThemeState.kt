package com.relaxmind.app.ui.themes

import kotlinx.coroutines.flow.MutableStateFlow

object ThemeState {
    val darkMode = MutableStateFlow(false)
    val language = MutableStateFlow("es")
}
