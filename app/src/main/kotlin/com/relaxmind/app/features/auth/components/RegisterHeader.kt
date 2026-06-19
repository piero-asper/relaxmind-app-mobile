package com.relaxmind.app.features.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.relaxmind.app.R
import com.relaxmind.app.ui.themes.LexendFontFamily
import com.relaxmind.app.ui.themes.PatientGreen
import com.relaxmind.app.ui.themes.TextPrimary
import com.relaxmind.app.ui.themes.TextSecondary

@Composable
fun RegisterHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_plano2),
                    contentDescription = "RelaxMind Logo",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "RelaxMind",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = LexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = PatientGreen
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = LexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Comienza tu espacio de bienestar 🌿",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = LexendFontFamily,
                    color = TextSecondary
                )
            )
        }

        Image(
            painter = painterResource(id = R.drawable.registro),
            contentDescription = "Ilustración 3D Registro",
            modifier = Modifier.size(130.dp),
            contentScale = ContentScale.Fit
        )
    }
}
