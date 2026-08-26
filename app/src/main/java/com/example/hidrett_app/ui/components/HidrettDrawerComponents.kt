package com.example.hidrett_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hidrett_app.ui.theme.HidrettAccent
import com.example.hidrett_app.ui.theme.HidrettTextPrimary
import com.example.hidrett_app.ui.theme.HidrettTextSecondary

@Composable
fun DrawerRow(
    label: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    trailingIcon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = HidrettAccent,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = HidrettTextPrimary)
        Spacer(modifier = Modifier.weight(1f))
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = HidrettTextSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun InitialAvatar(text: String, size: Dp) {
    val hue = text.hashCode()
    val color = Color(
        red = ((hue shr 16) and 0xFF) / 255f,
        green = ((hue shr 8) and 0xFF) / 255f,
        blue = (hue and 0xFF) / 255f,
        alpha = 1f
    )
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.take(1).uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}