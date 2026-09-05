package com.example.hidrett_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.hidrett_app.ui.models.Post
import com.example.hidrett_app.ui.models.VoteState
import com.example.hidrett_app.ui.theme.HidrettAccent
import com.example.hidrett_app.ui.theme.HidrettBackground
import com.example.hidrett_app.ui.theme.HidrettSurface
import com.example.hidrett_app.ui.theme.HidrettTextPrimary
import com.example.hidrett_app.ui.theme.HidrettTextSecondary

// A muted coral, distinct from HidrettAccent, used only for the active-downvote state.
private val HidrettDownvoteActive = Color(0xFFFF6B6B)

@Composable
fun PostCard(
    post: Post,
    onVote: (VoteState) -> Unit,
    onToggleSave: () -> Unit,
    onOpenPost: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
    showOverflowMenu: Boolean = true
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = HidrettSurface,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenPost)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InitialAvatar(text = post.communityName, size = 28.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.communityName,
                        color = HidrettAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${post.authorDisplay} · ${post.timeAgo}",
                        color = HidrettTextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (showOverflowMenu) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                contentDescription = "Post options",
                                tint = HidrettTextSecondary
                            )
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(text = { Text("Report") }, onClick = { menuExpanded = false })
                            DropdownMenuItem(text = { Text("Hide") }, onClick = { menuExpanded = false })
                            DropdownMenuItem(
                                text = { Text("Block ${post.authorDisplay}") },
                                onClick = { menuExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = post.title,
                color = HidrettTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            post.bodyPreview?.let { preview ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = preview,
                    color = HidrettTextSecondary,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )
            }

            post.imageUrls.firstOrNull()?.let { imageUrl ->
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .background(HidrettBackground)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            PostActionRow(
                post = post,
                onVote = onVote,
                onToggleSave = onToggleSave,
                onComment = onOpenPost,
                onShare = onShare
            )
        }
    }
}

@Composable
private fun PostActionRow(
    post: Post,
    onVote: (VoteState) -> Unit,
    onToggleSave: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = HidrettBackground, shape = RoundedCornerShape(50.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onVote(VoteState.UP) }, modifier = Modifier.size(34.dp)) {
                    Icon(
                        Icons.Outlined.ArrowUpward,
                        contentDescription = "Upvote",
                        tint = if (post.voteState == VoteState.UP) HidrettAccent else HidrettTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "${post.score}",
                    color = when (post.voteState) {
                        VoteState.UP -> HidrettAccent
                        VoteState.DOWN -> HidrettDownvoteActive
                        VoteState.NONE -> HidrettTextPrimary
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                IconButton(onClick = { onVote(VoteState.DOWN) }, modifier = Modifier.size(34.dp)) {
                    Icon(
                        Icons.Outlined.ArrowDownward,
                        contentDescription = "Downvote",
                        tint = if (post.voteState == VoteState.DOWN) HidrettDownvoteActive else HidrettTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onComment)
        ) {
            Icon(
                Icons.Outlined.ModeComment,
                contentDescription = "Comments",
                tint = HidrettTextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("${post.commentCount}", color = HidrettTextSecondary, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onShare) {
            Icon(
                Icons.Outlined.Share,
                contentDescription = "Share",
                tint = HidrettTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        IconButton(onClick = onToggleSave) {
            Icon(
                imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = if (post.isSaved) "Unsave" else "Save",
                tint = if (post.isSaved) HidrettAccent else HidrettTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}