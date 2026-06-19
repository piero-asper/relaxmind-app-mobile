package com.relaxmind.app.features.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Ancho compartido: ilustración, bienvenida y card de formulario. */
val LoginContentWidthFraction = 1f

/** Padding horizontal de la pantalla de login. */
val LoginScreenHorizontalPadding = 20.dp

@Composable
fun LoginAlignedContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(LoginContentWidthFraction),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}
