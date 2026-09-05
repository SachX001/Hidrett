package com.example.hidrett_app.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hidrett_app.ui.models.Post
import com.example.hidrett_app.ui.models.VoteState
import com.example.hidrett_app.ui.theme.HidrettBackground

@Composable
fun Feed(
    posts: List<Post>,
    onVote: (postId: String, tapped: VoteState) -> Unit,
    onToggleSave: (postId: String) -> Unit,
    onOpenPost: (postId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.background(HidrettBackground),
        // Extra bottom padding reserves room so the last post isn't hidden
        // behind the floating bottom nav bar.
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onVote = { tapped -> onVote(post.id, tapped) },
                onToggleSave = { onToggleSave(post.id) },
                onOpenPost = { onOpenPost(post.id) },
                onShare = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        // Post-ID only — carries no identity of who is sharing it.
                        putExtra(Intent.EXTRA_TEXT, "https://hidrett.app/post/${post.id}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share post"))
                }
            )
        }
    }
}