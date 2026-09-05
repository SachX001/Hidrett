package com.example.hidrett_app.ui.components

data class Notifications(
    val id: String,
    val type: NotificationType,
    val title: String,
    val preview: String?,
    val community: String?,
    val timestamp: String,
    val targetPostId: String,
    val isRead: Boolean,
    val author: String? = null
)

enum class NotificationType{
    Replies,
    Community
}