package com.example.hidrett_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hidrett_app.ui.theme.HidrettTextPrimary
import com.example.hidrett_app.ui.theme.HidrettTextSecondary

@Composable
fun NotificationRow(
    notification: Notifications,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (notification.isRead) {
                    Color.Transparent
                } else {
                    HidrettTextPrimary.copy(alpha = 0.04f)
                }
            )
            .padding(22.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (notification.type) {
            NotificationType.Community -> InitialAvatar(
                text = notification.community ?: "",
                size = 40.dp
            )
            NotificationType.Replies -> InitialAvatar(
                text = notification.author ?: notification.title,
                size = 40.dp
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = notification.title,
                color = HidrettTextPrimary,
                fontSize = 15.sp,
                fontWeight = if(notification.isRead) {
                    FontWeight.Normal
                } else {
                    FontWeight.SemiBold
                }
            )

            notification.preview?.let {
                Text(
                    text = it,
                    color = HidrettTextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = buildString {
                    notification.community?.let {
                        append(it)
                        append(" . ")
                    }
                    append(notification.timestamp)
                },
                color = HidrettTextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(6.dp)
            )
        }
    }

    HorizontalDivider(
        color = HidrettTextPrimary.copy(alpha = 0.08f)
    )
}