package com.example.hidrett_app.ui.models

data class Post(
    val id: String,
    val communityName: String,
    val authorDisplay: String,
    val timeAgo: String,
    val title: String,
    val bodyPreview: String? = null,
    val imageUrls: List<String> = emptyList(), // UI currently renders only imageUrls.firstOrNull()
    val score: Int,
    val voteState: VoteState = VoteState.NONE,
    val commentCount: Int,
    val isSaved: Boolean = false
)

enum class VoteState { UP, NONE, DOWN }

fun Post.withVote(tapped: VoteState): Post {
    val (newState, delta) = when {
        voteState == tapped -> VoteState.NONE to (if (tapped == VoteState.UP) -1 else 1)
        voteState == VoteState.NONE -> tapped to (if (tapped == VoteState.UP) 1 else -1)
        else -> tapped to (if (tapped == VoteState.UP) 2 else -2)
    }
    return copy(voteState = newState, score = score + delta)
}