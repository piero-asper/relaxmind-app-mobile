package com.relaxmind.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object RelaxIcons {
    val ArrowBack: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
    val Add: ImageVector = Icons.Filled.Add
    val Check: ImageVector = Icons.Filled.Check
    val Close: ImageVector = Icons.Filled.Close
    val Home: ImageVector = Icons.Filled.Home
    val Notifications: ImageVector = Icons.Filled.Notifications
    val Person: ImageVector = Icons.Filled.Person
    val Settings: ImageVector = Icons.Filled.Settings

    val Calendar: ImageVector = outlinedIcon("RelaxCalendar") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(7f, 3f)
            verticalLineTo(6f)
            moveTo(17f, 3f)
            verticalLineTo(6f)
            moveTo(4f, 9f)
            horizontalLineTo(20f)
            moveTo(6f, 5f)
            horizontalLineTo(18f)
            quadTo(20f, 5f, 20f, 7f)
            verticalLineTo(19f)
            quadTo(20f, 21f, 18f, 21f)
            horizontalLineTo(6f)
            quadTo(4f, 21f, 4f, 19f)
            verticalLineTo(7f)
            quadTo(4f, 5f, 6f, 5f)
        }
    }

    val Chat: ImageVector = outlinedIcon("RelaxChat") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(5f, 6f)
            horizontalLineTo(19f)
            quadTo(21f, 6f, 21f, 8f)
            verticalLineTo(16f)
            quadTo(21f, 18f, 19f, 18f)
            horizontalLineTo(10f)
            lineTo(5f, 21f)
            verticalLineTo(18f)
            quadTo(3f, 18f, 3f, 16f)
            verticalLineTo(8f)
            quadTo(3f, 6f, 5f, 6f)
            moveTo(7f, 10f)
            horizontalLineTo(17f)
            moveTo(7f, 14f)
            horizontalLineTo(13f)
        }
    }

    val Dashboard: ImageVector = outlinedIcon("RelaxDashboard") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(4f, 4f)
            horizontalLineTo(11f)
            verticalLineTo(11f)
            horizontalLineTo(4f)
            close()
            moveTo(13f, 4f)
            horizontalLineTo(20f)
            verticalLineTo(11f)
            horizontalLineTo(13f)
            close()
            moveTo(4f, 13f)
            horizontalLineTo(11f)
            verticalLineTo(20f)
            horizontalLineTo(4f)
            close()
            moveTo(13f, 13f)
            horizontalLineTo(20f)
            verticalLineTo(20f)
            horizontalLineTo(13f)
            close()
        }
    }

    val Email: ImageVector = outlinedIcon("RelaxEmail") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(4f, 6f)
            horizontalLineTo(20f)
            quadTo(21f, 6f, 21f, 7f)
            verticalLineTo(17f)
            quadTo(21f, 18f, 20f, 18f)
            horizontalLineTo(4f)
            quadTo(3f, 18f, 3f, 17f)
            verticalLineTo(7f)
            quadTo(3f, 6f, 4f, 6f)
            moveTo(4f, 7f)
            lineTo(12f, 13f)
            lineTo(20f, 7f)
        }
    }

    val Eye: ImageVector = outlinedIcon("RelaxEye") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(2.5f, 12f)
            quadTo(12f, 4.5f, 21.5f, 12f)
            quadTo(12f, 19.5f, 2.5f, 12f)
            moveTo(12f, 9f)
            arcToRelative(3f, 3f, 0f, true, true, 0f, 6f)
            arcToRelative(3f, 3f, 0f, true, true, 0f, -6f)
        }
    }

    val EyeOff: ImageVector = outlinedIcon("RelaxEyeOff") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(3f, 3f)
            lineTo(21f, 21f)
            moveTo(10.6f, 10.6f)
            quadTo(10f, 12f, 11f, 13f)
            quadTo(12f, 14f, 13.4f, 13.4f)
            moveTo(7.2f, 7.5f)
            quadTo(4.6f, 8.9f, 2.5f, 12f)
            quadTo(12f, 19.5f, 18f, 15.8f)
            moveTo(10f, 5.4f)
            quadTo(16f, 4.6f, 21.5f, 12f)
            quadTo(20.7f, 13.1f, 19.7f, 14.1f)
        }
    }

    val Fingerprint: ImageVector = outlinedIcon("RelaxFingerprint") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.4f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(7f, 8f)
            quadTo(12f, 4f, 17f, 8f)
            moveTo(5.5f, 11f)
            quadTo(12f, 5.8f, 18.5f, 11f)
            moveTo(8f, 13f)
            quadTo(12f, 9.8f, 16f, 13f)
            moveTo(10f, 16f)
            quadTo(12f, 17.5f, 14f, 16f)
            moveTo(12f, 12f)
            verticalLineTo(19f)
        }
    }

    val Groups: ImageVector = outlinedIcon("RelaxGroups") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(9f, 11f)
            arcToRelative(3f, 3f, 0f, true, false, 0f, -6f)
            arcToRelative(3f, 3f, 0f, false, false, 0f, 6f)
            moveTo(15f, 11f)
            arcToRelative(2.5f, 2.5f, 0f, true, false, 0f, -5f)
            arcToRelative(2.5f, 2.5f, 0f, false, false, 0f, 5f)
            moveTo(4f, 19f)
            quadTo(4.5f, 13f, 9f, 13f)
            quadTo(13.5f, 13f, 14f, 19f)
            close()
            moveTo(13.5f, 19f)
            quadTo(14f, 14.5f, 18f, 14.5f)
            quadTo(21f, 14.5f, 21.5f, 19f)
            close()
        }
    }

    val Lock: ImageVector = outlinedIcon("RelaxLock") {
        path(fill = SolidColor(Color.Black)) {
            moveTo(7f, 10f)
            verticalLineTo(8f)
            quadTo(7f, 3f, 12f, 3f)
            quadTo(17f, 3f, 17f, 8f)
            verticalLineTo(10f)
            horizontalLineTo(18f)
            quadTo(20f, 10f, 20f, 12f)
            verticalLineTo(20f)
            quadTo(20f, 22f, 18f, 22f)
            horizontalLineTo(6f)
            quadTo(4f, 22f, 4f, 20f)
            verticalLineTo(12f)
            quadTo(4f, 10f, 6f, 10f)
            close()
            moveTo(9f, 10f)
            horizontalLineTo(15f)
            verticalLineTo(8f)
            quadTo(15f, 5f, 12f, 5f)
            quadTo(9f, 5f, 9f, 8f)
            close()
        }
    }

    val Meditation: ImageVector = outlinedIcon("RelaxMeditation") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(12f, 5f)
            arcToRelative(2f, 2f, 0f, true, true, 0f, 4f)
            arcToRelative(2f, 2f, 0f, true, true, 0f, -4f)
            moveTo(12f, 10f)
            verticalLineTo(14f)
            moveTo(8f, 12f)
            lineTo(16f, 12f)
            moveTo(5f, 19f)
            quadTo(9f, 15f, 12f, 18f)
            quadTo(15f, 15f, 19f, 19f)
        }
    }

    val Progress: ImageVector = outlinedIcon("RelaxProgress") {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(4f, 19f)
            lineTo(9f, 14f)
            lineTo(13f, 16f)
            lineTo(20f, 7f)
            moveTo(20f, 7f)
            horizontalLineTo(15f)
            moveTo(20f, 7f)
            verticalLineTo(12f)
        }
    }

    private inline fun outlinedIcon(
        name: String,
        block: ImageVector.Builder.() -> Unit
    ): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply(block).build()
}
