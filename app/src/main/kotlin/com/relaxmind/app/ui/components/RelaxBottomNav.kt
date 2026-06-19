package com.relaxmind.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.relaxmind.app.ui.themes.LexendFontFamily
import com.relaxmind.app.ui.themes.PatientGreen
import com.relaxmind.app.ui.themes.RelaxMindTheme

@Composable
fun RelaxBottomNav(
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    role: AppRole
) {
    val items = when (role) {
        AppRole.PATIENT -> patientNavItems
        AppRole.CAREGIVER -> caregiverNavItems
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(32.dp),
                    clip = false,
                    ambientColor = Color(0xFF8A88A6).copy(alpha = 0.35f),
                    spotColor = Color(0xFF8A88A6).copy(alpha = 0.35f)
                )
                .background(Color(0xFFFDFBFF), RoundedCornerShape(32.dp))
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedRoute == item.route

                if (role == AppRole.PATIENT && index == 2) {
                    // Center highlighted button for Progress
                    CenterProgressNavButton(
                        item = item,
                        isSelected = isSelected,
                        onClick = { onNavigate(item.route) }
                    )
                } else {
                    NormalNavItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NormalNavItem(
    item: RelaxNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = if (item.route.startsWith("caregiver")) Color(0xFF2B6CB0) else PatientGreen
    val inactiveColor = Color(0xFF8A88A6)
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        label = "nav-item-color"
    )

    Column(
        modifier = Modifier
            .width(56.dp)
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.label,
            fontFamily = LexendFontFamily,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 11.sp,
            color = contentColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        // Active indicator dot
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(
                    color = if (isSelected) activeColor else Color.Transparent,
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun CenterProgressNavButton(
    item: RelaxNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = PatientGreen
    val inactiveColor = Color(0xFF8A88A6)

    Box(
        modifier = Modifier
            .width(64.dp)
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            // Elevated green button
            Box(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .size(54.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        ambientColor = PatientGreen.copy(alpha = 0.4f),
                        spotColor = PatientGreen.copy(alpha = 0.4f)
                    )
                    .background(PatientGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Text(
                text = item.label,
                fontFamily = LexendFontFamily,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 11.sp,
                color = if (isSelected) activeColor else inactiveColor,
                modifier = Modifier.offset(y = (-6).dp)
            )

            // Active indicator dot
            Box(
                modifier = Modifier
                    .offset(y = (-4).dp)
                    .size(4.dp)
                    .background(
                        color = if (isSelected) activeColor else Color.Transparent,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Immutable
private data class RelaxNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val patientNavItems = listOf(
    RelaxNavItem("patient/dashboard", "Inicio", RelaxIcons.Home),
    RelaxNavItem("patient/meditate", "Meditar", RelaxIcons.Meditation),
    RelaxNavItem("patient/progress", "Progreso", RelaxIcons.Progress),
    RelaxNavItem("patient/schedule", "Agenda", RelaxIcons.Calendar),
    RelaxNavItem("patient/settings", "Ajustes", RelaxIcons.Settings)
)

private val caregiverNavItems = listOf(
    RelaxNavItem("caregiver/dashboard", "Inicio", RelaxIcons.Home),
    RelaxNavItem("caregiver/patients", "Pacientes", RelaxIcons.Groups),
    RelaxNavItem("caregiver/alerts", "Alertas", RelaxIcons.Notifications),
    RelaxNavItem("caregiver/settings", "Ajustes", RelaxIcons.Settings)
)

@Preview(name = "RelaxBottomNav Patient Light", showBackground = true)
@Composable
private fun RelaxBottomNavPatientLightPreview() {
    RelaxMindTheme(darkTheme = false) {
        RelaxBottomNav(
            selectedRoute = "patient/dashboard",
            onNavigate = {},
            role = AppRole.PATIENT
        )
    }
}
