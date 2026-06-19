package com.relaxmind.app.features.patient

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.relaxmind.app.data.model.DiaryEntry
import com.relaxmind.app.ui.components.AppRole
import com.relaxmind.app.ui.components.ButtonVariant
import com.relaxmind.app.ui.components.FullScreenLoadingOverlay
import com.relaxmind.app.ui.components.RelaxButton
import com.relaxmind.app.ui.components.RelaxCard
import com.relaxmind.app.ui.components.RelaxTopBar
import com.relaxmind.app.ui.themes.PatientGreen
import com.relaxmind.app.ui.themes.SOSCoral
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DiaryScreen(
    viewModel: PatientViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onCreateEntry: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val entries by viewModel.diaryEntries.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDiaryEntries()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            RelaxTopBar(
                title = "Mi Diario",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DiaryHeader(
                        totalEntries = entries.size,
                        onCreateEntry = onCreateEntry
                    )
                }

                if (error != null) {
                    item {
                        Text(
                            text = error.orEmpty(),
                            color = SOSCoral,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                if (!isLoading && entries.isEmpty()) {
                    item {
                        EmptyDiaryState(onCreateEntry = onCreateEntry)
                    }
                }

                items(entries, key = { it.id.ifBlank { "${it.date}-${it.notes}" } }) { entry ->
                    DiaryHistoryCard(entry = entry)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            if (isLoading) {
                FullScreenLoadingOverlay()
            }
        }
    }
}

@Composable
private fun DiaryHeader(
    totalEntries: Int,
    onCreateEntry: () -> Unit
) {
    RelaxCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tus notas emocionales",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                    fontWeight = FontWeight.Bold,
                    color = PatientGreen
                )
                Text(
                    text = "$totalEntries entradas guardadas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                )
            }
            RelaxButton(
                text = "Nueva",
                onClick = onCreateEntry,
                variant = ButtonVariant.PRIMARY,
                role = AppRole.PATIENT
            )
        }
    }
}

@Composable
private fun EmptyDiaryState(onCreateEntry: () -> Unit) {
    RelaxCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(PatientGreen.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = PatientGreen
                )
            }
            Text(
                text = "Aún no tienes notas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Cuando escribas una entrada, aparecerá aquí con sus fotos y detalles.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )
            RelaxButton(
                text = "Escribir primera entrada",
                onClick = onCreateEntry,
                variant = ButtonVariant.OUTLINE,
                role = AppRole.PATIENT
            )
        }
    }
}

@Composable
private fun DiaryHistoryCard(entry: DiaryEntry) {
    RelaxCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(PatientGreen.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = PatientGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = formatDiaryDate(entry.date),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Estado: ${entry.emotion}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(PatientGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = entry.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = PatientGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (entry.notes.isNotBlank()) {
                Text(
                    text = entry.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (entry.photoUrls.isNotEmpty()) {
                DiaryPhotoCollage(photoUrls = entry.photoUrls.take(5))
            }
        }
    }
}

@Composable
private fun DiaryPhotoCollage(
    photoUrls: List<String>,
    modifier: Modifier = Modifier
) {
    when (photoUrls.size) {
        1 -> CollageImage(photoUrls[0], modifier.fillMaxWidth().height(190.dp))
        2 -> Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CollageImage(photoUrls[0], Modifier.weight(1f).height(132.dp))
            CollageImage(photoUrls[1], Modifier.weight(1f).height(132.dp))
        }
        3 -> Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CollageImage(photoUrls[0], Modifier.weight(1.4f).height(172.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CollageImage(photoUrls[1], Modifier.fillMaxWidth().height(82.dp))
                CollageImage(photoUrls[2], Modifier.fillMaxWidth().height(82.dp))
            }
        }
        4 -> Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CollageImage(photoUrls[0], Modifier.weight(1f).height(104.dp))
                CollageImage(photoUrls[1], Modifier.weight(1f).height(104.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CollageImage(photoUrls[2], Modifier.weight(1f).height(104.dp))
                CollageImage(photoUrls[3], Modifier.weight(1f).height(104.dp))
            }
        }
        else -> Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CollageImage(photoUrls[0], Modifier.weight(1.2f).height(184.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CollageImage(photoUrls[1], Modifier.weight(1f).height(88.dp))
                    CollageImage(photoUrls[2], Modifier.weight(1f).height(88.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CollageImage(photoUrls[3], Modifier.weight(1f).height(88.dp))
                    CollageImage(photoUrls[4], Modifier.weight(1f).height(88.dp))
                }
            }
        }
    }
}

@Composable
private fun CollageImage(
    url: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(14.dp))
    )
}

private fun formatDiaryDate(date: String): String {
    return runCatching {
        val parsed = LocalDate.parse(date)
        val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "ES"))
        parsed.format(formatter).replaceFirstChar { it.uppercase() }
    }.getOrDefault(date)
}
