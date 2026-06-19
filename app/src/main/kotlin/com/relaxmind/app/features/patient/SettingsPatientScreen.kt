package com.relaxmind.app.features.patient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.relaxmind.app.MainActivity
import com.relaxmind.app.ui.components.AppRole
import com.relaxmind.app.ui.components.ButtonVariant
import com.relaxmind.app.ui.components.FullScreenLoadingOverlay
import com.relaxmind.app.ui.components.RelaxBottomNav
import com.relaxmind.app.ui.components.RelaxButton
import com.relaxmind.app.ui.components.RelaxCard
import com.relaxmind.app.ui.components.RelaxTopBar
import com.relaxmind.app.ui.themes.PatientGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPatientScreen(
    viewModel: PatientViewModel = viewModel(),
    onNavigateToEditProfile: () -> Unit,
    onLogout: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val patient by viewModel.patient.collectAsState()
    val caregiver by viewModel.caregiver.collectAsState()

    var showLanguageBottomSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showUnlinkDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    Scaffold(
        topBar = {
            RelaxTopBar(title = "Ajustes")
        },
        bottomBar = {
            RelaxBottomNav(
                selectedRoute = "patient/settings",
                onNavigate = onNavigate,
                role = AppRole.PATIENT
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                patient?.let { currPatient ->
                    // SECCIÓN "MI PERFIL"
                    RelaxCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateToEditProfile)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                PatientAvatar(avatarUrl = currPatient.avatarUrl)
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "${currPatient.name} ${currPatient.lastName}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = currPatient.email,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // SECCIÓN "APARIENCIA"
                    SectionTitle("Apariencia")
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        SettingsToggleRow(
                            label = "Modo oscuro",
                            icon = Icons.Filled.DarkMode,
                            checked = currPatient.darkMode,
                            onToggle = { viewModel.updateDarkMode(it) }
                        )
                    }

                    // SECCIÓN "IDIOMA"
                    SectionTitle("Idioma")
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        SettingsRow(
                            label = "Idioma",
                            icon = Icons.Filled.Language,
                            trailingText = if (currPatient.language == "en") "English" else "Español",
                            onClick = { showLanguageBottomSheet = true }
                        )
                    }

                    // SECCIÓN "NOTIFICACIONES"
                    SectionTitle("Notificaciones")
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SettingsToggleRow(
                                label = "Notificaciones",
                                icon = Icons.Filled.Notifications,
                                checked = currPatient.notificationsEnabled,
                                onToggle = { viewModel.updateNotificationsEnabled(it) }
                            )
                            if (currPatient.notificationsEnabled) {
                                Spacer(modifier = Modifier.height(4.dp))
                                SettingsToggleRow(
                                    label = "Recordatorio de check-in diario",
                                    icon = Icons.Filled.AccessTime,
                                    checked = currPatient.checkInReminderEnabled,
                                    onToggle = { viewModel.updateCheckInReminderEnabled(it) }
                                )
                            }
                        }
                    }

                    // SECCIÓN "SEGURIDAD"
                    SectionTitle("Seguridad")
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        SettingsToggleRow(
                            label = "Inicio con biometría",
                            icon = Icons.Filled.Fingerprint,
                            checked = currPatient.biometricEnabled,
                            onToggle = { viewModel.updateBiometricEnabled(it) }
                        )
                    }

                    // SECCIÓN "DATOS PERSONALES"
                    SectionTitle("Datos personales")
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            if (currPatient.caregiverId != null) {
                                SettingsRow(
                                    label = "Desvincular cuidador",
                                    icon = Icons.Filled.LinkOff,
                                    color = Color(0xFFED8936),
                                    onClick = { showUnlinkDialog = true }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            SettingsRow(
                                label = "Borrar cuenta",
                                icon = Icons.Filled.Delete,
                                color = Color.Gray,
                                onClick = { showDeleteAccountDialog = true }
                            )
                        }
                    }

                    // SECCIÓN "INFORMACIÓN"
                    SectionTitle("Información")
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SettingsRow(
                                label = "Términos y condiciones",
                                icon = Icons.Filled.Description,
                                onClick = { openTermsUrl(context) }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            SettingsRow(
                                label = "Versión de la app",
                                icon = Icons.Filled.Info,
                                trailingContent = {
                                    Text(
                                        text = "1.0.0",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            )
                        }
                    }

                    // SECCIÓN "SESIÓN"
                    SectionTitle("Sesión")
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        SettingsRow(
                            label = "Cerrar sesión",
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            color = Color.Red,
                            onClick = { showLogoutDialog = true }
                        )
                    }
                }
            }

            if (isLoading) {
                FullScreenLoadingOverlay()
            }
        }
    }

    // Modal Bottom Sheet para idiomas
    if (showLanguageBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLanguageBottomSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Selecciona tu idioma",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.updateLanguage("es")
                            showLanguageBottomSheet = false
                            relaunchActivity(context)
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Español (ES)",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (patient?.language == "es") FontWeight.Bold else FontWeight.Normal,
                        color = if (patient?.language == "es") PatientGreen else MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.updateLanguage("en")
                            showLanguageBottomSheet = false
                            relaunchActivity(context)
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "English (EN)",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (patient?.language == "en") FontWeight.Bold else FontWeight.Normal,
                        color = if (patient?.language == "en") PatientGreen else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    // Diálogos de confirmación
    if (showLogoutDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("¿Cerrar sesión?") },
            text = { Text("Tendrás que ingresar de nuevo.") },
            confirmButton = {
                RelaxButton(
                    text = "Confirmar",
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    role = AppRole.PATIENT
                )
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showUnlinkDialog) {
        var password by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                showUnlinkDialog = false
                password = ""
                errorMessage = null
            },
            title = { Text("Desvincular cuidador") },
            text = {
                Column {
                    Text("¿Deseas desvincularte de ${caregiver?.let { "${it.name} ${it.lastName}" } ?: "tu cuidador"}?")
                    Spacer(modifier = Modifier.height(12.dp))
                    androidx.compose.material3.OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña para confirmar") },
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        isError = errorMessage != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                RelaxButton(
                    text = "Desvincular",
                    onClick = {
                        viewModel.unlinkCaregiver(
                            passwordConfirm = password,
                            onSuccess = {
                                showUnlinkDialog = false
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    },
                    role = AppRole.PATIENT
                )
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showUnlinkDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDeleteAccountDialog) {
        var deleteStep by remember { mutableStateOf(1) }
        var selectedReason by remember { mutableStateOf("Ya no lo necesito") }
        var otherReasonDetail by remember { mutableStateOf("") }
        var dropdownExpanded by remember { mutableStateOf(false) }
        var password by remember { mutableStateOf("") }
        var deleteErrorMessage by remember { mutableStateOf<String?>(null) }

        val reasons = listOf("Ya no lo necesito", "Problemas técnicos", "Privacidad", "Otro")

        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                showDeleteAccountDialog = false
                deleteStep = 1
                password = ""
                deleteErrorMessage = null
            },
            title = {
                Text(
                    text = when (deleteStep) {
                        1 -> "¿Estás seguro?"
                        2 -> "Motivo de eliminación"
                        else -> "Confirmar eliminación"
                    }
                )
            },
            text = {
                Column {
                    when (deleteStep) {
                        1 -> {
                            Text("Esta acción eliminará permanentemente tu cuenta y todos tus datos asociados. No podrás deshacer esta acción.")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("• Perderás tu historial de check-in.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            Text("• Se desvinculará tu cuidador.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            Text("• Se perderán tus metas y citas.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        }
                        2 -> {
                            Text("Por favor, indícanos por qué deseas borrar tu cuenta:")
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(modifier = Modifier.fillMaxWidth()) {
                                androidx.compose.material3.OutlinedButton(
                                    onClick = { dropdownExpanded = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(selectedReason, color = MaterialTheme.colorScheme.onSurface)
                                        Icon(
                                            imageVector = Icons.Filled.ArrowDropDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                androidx.compose.material3.DropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false },
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                ) {
                                    reasons.forEach { reason ->
                                        androidx.compose.material3.DropdownMenuItem(
                                            text = { Text(reason) },
                                            onClick = {
                                                selectedReason = reason
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (selectedReason == "Otro") {
                                Spacer(modifier = Modifier.height(12.dp))
                                androidx.compose.material3.OutlinedTextField(
                                    value = otherReasonDetail,
                                    onValueChange = { otherReasonDetail = it },
                                    label = { Text("Detalla tu motivo") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        3 -> {
                            Text("Ingresa tu contraseña para confirmar la eliminación de tu cuenta:")
                            Spacer(modifier = Modifier.height(12.dp))
                            androidx.compose.material3.OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Contraseña") },
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                isError = deleteErrorMessage != null,
                                modifier = Modifier.fillMaxWidth()
                           )
                           if (deleteErrorMessage != null) {
                               Text(
                                   text = deleteErrorMessage ?: "",
                                   color = MaterialTheme.colorScheme.error,
                                   style = MaterialTheme.typography.bodySmall,
                                   modifier = Modifier.padding(top = 4.dp)
                               )
                           }
                       }
                   }
               }
           },
           confirmButton = {
               RelaxButton(
                   text = when (deleteStep) {
                       1 -> "Siguiente"
                       2 -> "Siguiente"
                       else -> "Confirmar"
                   },
                   onClick = {
                       when (deleteStep) {
                           1 -> deleteStep = 2
                           2 -> deleteStep = 3
                           3 -> {
                               val finalReason = if (selectedReason == "Otro") {
                                   "Otro: $otherReasonDetail"
                               } else {
                                   selectedReason
                               }
                               viewModel.deleteAccount(
                                   reason = finalReason,
                                   passwordConfirm = password,
                                   onSuccess = {
                                       showDeleteAccountDialog = false
                                       onLogout()
                                   },
                                   onError = { error ->
                                       deleteErrorMessage = error
                                   }
                               )
                           }
                       }
                   },
                   role = AppRole.PATIENT
               )
           },
           dismissButton = {
               androidx.compose.material3.TextButton(
                   onClick = {
                       if (deleteStep > 1) {
                           deleteStep--
                       } else {
                           showDeleteAccountDialog = false
                       }
                   }
               ) {
                   Text(if (deleteStep > 1) "Atrás" else "Cancelar")
               }
           }
       )
   }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        color = Color.Gray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingsRow(
    label: String,
    icon: ImageVector? = null,
    color: Color? = null,
    trailingText: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color ?: MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = color ?: MaterialTheme.colorScheme.onSurface
            )
        }

        if (trailingContent != null) {
            trailingContent()
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (trailingText != null) {
                    Text(
                        text = trailingText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                if (onClick != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    icon: ImageVector,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        androidx.compose.material3.Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = androidx.compose.material3.SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PatientGreen
            )
        )
    }
}

@Composable
private fun PatientAvatar(avatarUrl: String) {
    val isCustomAvatar = avatarUrl.startsWith("relaxmind://avatar/")
    val modifier = Modifier
        .size(52.dp)
        .clip(CircleShape)
        .border(1.5.dp, PatientGreen.copy(alpha = 0.3f), CircleShape)

    if (isCustomAvatar) {
        val colors = getAvatarColors(avatarUrl)
        Box(
            modifier = modifier.background(Brush.linearGradient(colors))
        )
    } else {
        AsyncImage(
            model = avatarUrl.ifBlank { "https://ui-avatars.com/api/?name=P&background=0F6E56&color=fff" },
            contentDescription = "Perfil",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

private fun getAvatarColors(url: String): List<Color> {
    return when (url) {
        "relaxmind://avatar/01" -> listOf(Color(0xFFA7F3D0), Color(0xFF0F6E56))
        "relaxmind://avatar/02" -> listOf(Color(0xFFFFD6A5), Color(0xFFED8936))
        "relaxmind://avatar/03" -> listOf(Color(0xFFD8B4FE), Color(0xFF7C3AED))
        "relaxmind://avatar/04" -> listOf(Color(0xFFA5F3FC), Color(0xFF0891B2))
        "relaxmind://avatar/05" -> listOf(Color(0xFFFBCFE8), Color(0xFFDB2777))
        "relaxmind://avatar/06" -> listOf(Color(0xFFBFDBFE), Color(0xFF2563EB))
        "relaxmind://avatar/07" -> listOf(Color(0xFFFEF3C7), Color(0xFFEAB308))
        "relaxmind://avatar/08" -> listOf(Color(0xFFFECACA), Color(0xFFEF4444))
        "relaxmind://avatar/09" -> listOf(Color(0xFFCCFBF1), Color(0xFF14B8A6))
        "relaxmind://avatar/10" -> listOf(Color(0xFFFED7AA), Color(0xFFEA580C))
        "relaxmind://avatar/11" -> listOf(Color(0xFFE9D5FF), Color(0xFFA855F7))
        "relaxmind://avatar/12" -> listOf(Color(0xFFFDE68A), Color(0xFFB45309))
        else -> listOf(Color(0xFFCBD5E0), Color(0xFF718096))
    }
}

private fun relaunchActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
    (context as? Activity)?.finish()
}

private fun openTermsUrl(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://relaxmind.com/terms"))
    context.startActivity(intent)
}
