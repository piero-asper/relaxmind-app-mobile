package com.relaxmind.app.features.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.relaxmind.app.R
import com.relaxmind.app.ui.components.RelaxIcons
import com.relaxmind.app.ui.themes.PatientGreen
import com.relaxmind.app.ui.themes.SoftMint

@Composable
fun LoginHeroIllustration(
    modifier: Modifier = Modifier
) {
    val heroAvailable = runCatching { R.drawable.login_hero }.isSuccess

    if (heroAvailable) {
        Image(
            painter = painterResource(id = R.drawable.login_hero),
            contentDescription = "Ilustración de persona meditando",
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1.48f),
            contentScale = ContentScale.Fit
        )
    } else {
        // TODO: reemplazar por ilustración 3D clay login_hero.png
        LoginHeroFallback(modifier = modifier)
    }
}

@Composable
private fun LoginHeroFallback(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.48f)
            .clip(RoundedCornerShape(32.dp))
            .background(SoftMint),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = RelaxIcons.Meditation,
            contentDescription = "Ilustración de meditación",
            tint = PatientGreen,
            modifier = Modifier.fillMaxWidth(0.3f)
        )
    }
}
