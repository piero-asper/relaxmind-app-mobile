package com.relaxmind.app.features.patient

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.relaxmind.app.data.model.CheckIn
import com.relaxmind.app.ui.components.AppRole
import com.relaxmind.app.ui.components.RelaxBottomNav
import com.relaxmind.app.ui.components.RelaxCard
import com.relaxmind.app.ui.components.RelaxTopBar
import com.relaxmind.app.ui.components.WellnessCalendarGrid
import com.relaxmind.app.ui.themes.PatientGreen
import com.relaxmind.app.ui.themes.ScoreGray
import com.relaxmind.app.ui.themes.ScoreGreenLight
import com.relaxmind.app.ui.themes.ScoreYellow
import com.relaxmind.app.utils.WellnessScoreCalculator
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private data class AchievementCatalogItem(
    val key: String,
    val title: String,
    val condition: String,
    val defaultIconUrl: String
)

private val AchievementCatalog = listOf(
    AchievementCatalogItem("first_checkin", "Primer paso", "Primer check-in completado", "https://cdn-icons-png.flaticon.com/512/825/825590.png"),
    AchievementCatalogItem("streak_3", "3 días seguidos", "Racha de 3 días", "https://cdn-icons-png.flaticon.com/512/785/785116.png"),
    AchievementCatalogItem("streak_7", "Una semana fuerte", "Racha de 7 días", "https://cdn-icons-png.flaticon.com/512/785/785116.png"),
    AchievementCatalogItem("streak_14", "Dos semanas imparable", "Racha de 14 días", "https://cdn-icons-png.flaticon.com/512/785/785116.png"),
    AchievementCatalogItem("streak_30", "Mes completo", "Racha de 30 días", "https://cdn-icons-png.flaticon.com/512/3112/3112946.png"),
    AchievementCatalogItem("first_meditation", "Primer respiro", "Primera meditación completada", "https://cdn-icons-png.flaticon.com/512/2913/2913520.png"),
    AchievementCatalogItem("meditations_10", "Mente en calma", "10 meditaciones completadas", "https://cdn-icons-png.flaticon.com/512/414/414927.png"),
    AchievementCatalogItem("first_diary", "Mi historia", "Primera entrada de diario", "https://cdn-icons-png.flaticon.com/512/3068/3068327.png"),
    AchievementCatalogItem("diary_7", "Una semana de notas", "7 entradas de diario", "https://cdn-icons-png.flaticon.com/512/3068/3068327.png"),
    AchievementCatalogItem("score_80", "Bienestar alto", "Check-in con 80+ puntos", "https://cdn-icons-png.flaticon.com/512/1828/1828884.png"),
    AchievementCatalogItem("score_100", "Día perfecto", "Check-in con 100 puntos", "https://cdn-icons-png.flaticon.com/512/616/616489.png"),
    AchievementCatalogItem("lumi_first", "Hola Lumi", "Primera conversación con Lumi", "https://cdn-icons-png.flaticon.com/512/134/134914.png")
)

@Composable
fun ProgressScreen(
    viewModel: PatientViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val streakData by viewModel.streak.collectAsState()
    val unlockedAchievements by viewModel.achievements.collectAsState()
    val allCheckIns by viewModel.allCheckIns.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()

    val currentStreak = streakData?.currentStreak ?: 0
    val longestStreak = streakData?.longestStreak ?: 0

    LaunchedEffect(Unit) {
        viewModel.loadProgressData()
    }

    // Filter check-ins by selected month/year
    val monthlyCheckIns = remember(allCheckIns, selectedMonth, selectedYear) {
        allCheckIns.filter { checkIn ->
            runCatching {
                val date = LocalDate.parse(checkIn.date)
                date.monthValue == selectedMonth && date.year == selectedYear
            }.getOrDefault(false)
        }
    }

    // Map dayOfMonth to score
    val checkInsMap = remember(monthlyCheckIns) {
        monthlyCheckIns.associate { checkIn ->
            runCatching {
                val date = LocalDate.parse(checkIn.date)
                date.dayOfMonth to checkIn.score
            }.getOrDefault(1 to 0)
        }
    }

    Scaffold(
        topBar = {
            RelaxTopBar(title = "Mi Progreso")
        },
        bottomBar = {
            RelaxBottomNav(
                selectedRoute = "patient/progress",
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
            if (isLoading && allCheckIns.isEmpty() && streakData == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PatientGreen)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // SECCIÓN RACHA
                    Text(
                        text = "Racha",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                        fontWeight = FontWeight.Bold
                    )
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            StreakLottieAnimation(streak = currentStreak)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$currentStreak",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = PatientGreen
                            )
                            Text(
                                text = "días seguidos",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mejor racha: $longestStreak días",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    // SECCIÓN GRÁFICO MENSUAL
                    Text(
                        text = "Mi progreso mensual",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                        fontWeight = FontWeight.Bold
                    )
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Month Selector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { viewModel.selectPreviousMonth() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                        contentDescription = "Mes anterior",
                                        tint = PatientGreen
                                    )
                                }
                                Text(
                                    text = "${getMonthNameInSpanish(selectedMonth)} $selectedYear",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PatientGreen
                                )
                                IconButton(onClick = { viewModel.selectNextMonth() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Mes siguiente",
                                        tint = PatientGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (monthlyCheckIns.isEmpty()) {
                                Text(
                                    text = "Aún no hay registros este mes.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 40.dp)
                                )
                            } else {
                                WellnessCalendarGrid(
                                    year = selectedYear,
                                    month = selectedMonth,
                                    checkIns = checkInsMap
                                )
                            }
                        }
                    }

                    // SECCIÓN LOGROS
                    Text(
                        text = "Logros",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                        fontWeight = FontWeight.Bold
                    )
                    RelaxCard(modifier = Modifier.fillMaxWidth()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(440.dp), // Fits 4 rows of 3 items comfortably
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(AchievementCatalog) { item ->
                                val unlockedAchievement = unlockedAchievements.find { it.achievementKey == item.key }
                                val isUnlocked = unlockedAchievement != null

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        AsyncImage(
                                            model = item.defaultIconUrl,
                                            contentDescription = item.title,
                                            modifier = Modifier.size(56.dp),
                                            colorFilter = if (!isUnlocked) ColorFilter.colorMatrix(
                                                ColorMatrix().apply { setToSaturation(0f) }
                                            ) else null
                                        )
                                        if (!isUnlocked) {
                                            Icon(
                                                imageVector = Icons.Filled.Lock,
                                                contentDescription = "Bloqueado",
                                                tint = Color.Gray.copy(alpha = 0.9f),
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .align(Alignment.BottomEnd)
                                                    .background(Color.White, CircleShape)
                                                    .padding(2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray,
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = if (isUnlocked) "Completado" else item.condition,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isUnlocked) PatientGreen else Color.LightGray,
                                        textAlign = TextAlign.Center,
                                        fontSize = 10.sp,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }

                    // SECCIÓN HISTORIAL
                    Text(
                        text = "Historial de check-ins",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                        fontWeight = FontWeight.Bold
                    )

                    if (allCheckIns.isEmpty()) {
                        Text(
                            text = "No tienes registros de check-in históricos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    } else {
                        allCheckIns.forEach { checkIn ->
                            CheckInHistoryRow(checkIn = checkIn)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StreakLottieAnimation(streak: Int) {
    // Standard public Lottie flame animation URLs
    val animationUrl = if (streak > 0) {
        "https://lottie.host/e02c6ef9-fa98-4c91-b3b3-8db8c56cc6de/ZshH3B5v3z.json" // flame animation
    } else {
        "https://lottie.host/8040bc18-2e06-4b95-a228-5696d5951d95/2bV2sZ6a00.json" // sleeping / static flame
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.Url(animationUrl))

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(100.dp)
        )
    } else {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = null,
            tint = if (streak > 0) Color(0xFFED8936) else Color.Gray,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
private fun CheckInHistoryRow(checkIn: CheckIn) {
    val dateFormatted = remember(checkIn.date) {
        runCatching {
            val date = LocalDate.parse(checkIn.date)
            val dayOfMonth = date.dayOfMonth
            val month = date.format(DateTimeFormatter.ofPattern("MMMM", java.util.Locale("es", "ES")))
            val year = date.year
            "$dayOfMonth de $month, $year"
        }.getOrDefault(checkIn.date)
    }

    val chipColor = WellnessScoreCalculator.getScoreColor(checkIn.score)
    val isLightBackground = chipColor == Color(0xFFECC94B) || chipColor == ScoreGray || chipColor == ScoreGreenLight
    val textColor = if (isLightBackground) Color(0xFF2D3748) else Color.White

    RelaxCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = PatientGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = dateFormatted,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Score Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(chipColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${checkIn.score} / 100",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = textColor
                    )
                }
                Text(
                    text = checkIn.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.width(80.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

private fun getMonthNameInSpanish(month: Int): String {
    return when (month) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        else -> "Diciembre"
    }
}
