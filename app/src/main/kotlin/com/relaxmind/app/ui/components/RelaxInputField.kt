package com.relaxmind.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import com.relaxmind.app.ui.themes.RelaxMindTheme
import com.relaxmind.app.ui.themes.SOSCoral

@Composable
fun RelaxInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: ImageVector? = null,
    role: AppRole = AppRole.PATIENT,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val accentColor = role.primaryColor()
    val fieldShape = RoundedCornerShape(18.dp)

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isError) 1.dp else 5.dp,
                    shape = fieldShape,
                    ambientColor = accentColor.copy(alpha = 0.22f),
                    spotColor = accentColor.copy(alpha = 0.18f)
                ),
            label = { Text(text = label) },
            shape = fieldShape,
            isError = isError,
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(accentColor.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = accentColor.copy(alpha = 0.58f),
                focusedLabelColor = accentColor,
                unfocusedLabelColor = accentColor.copy(alpha = 0.88f),
                cursorColor = accentColor,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorBorderColor = SOSCoral,
                errorLabelColor = SOSCoral,
                errorCursorColor = SOSCoral
            )
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                color = SOSCoral,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(name = "RelaxInputField Light", showBackground = true)
@Composable
private fun RelaxInputFieldLightPreview() {
    RelaxMindTheme(darkTheme = false) {
        RelaxInputField(
            value = "paciente@mail.com",
            onValueChange = {},
            label = "Correo",
            leadingIcon = RelaxIcons.Email
        )
    }
}

@Preview(name = "RelaxInputField Dark", showBackground = true)
@Composable
private fun RelaxInputFieldDarkPreview() {
    RelaxMindTheme(darkTheme = true) {
        RelaxInputField(
            value = "correo",
            onValueChange = {},
            label = "Correo",
            isError = true,
            errorMessage = "Ingresa un correo valido",
            leadingIcon = RelaxIcons.Email
        )
    }
}
