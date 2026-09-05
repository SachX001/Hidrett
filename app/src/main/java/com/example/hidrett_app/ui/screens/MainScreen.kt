package com.example.hidrett_app.ui.screens.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewHeadline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hidrett_app.ui.components.DrawerRow
import com.example.hidrett_app.ui.components.InitialAvatar
import com.example.hidrett_app.ui.components.NotificationRow
import com.example.hidrett_app.ui.components.NotificationTab
import com.example.hidrett_app.ui.components.NotificationType
import com.example.hidrett_app.ui.components.Notifications
import com.example.hidrett_app.ui.components.PostCard
import com.example.hidrett_app.ui.models.Post
import com.example.hidrett_app.ui.models.VoteState
import com.example.hidrett_app.ui.models.withVote
import com.example.hidrett_app.ui.theme.HidrettAccent
import com.example.hidrett_app.ui.theme.HidrettBackground
import com.example.hidrett_app.ui.theme.HidrettSurface
import com.example.hidrett_app.ui.theme.HidrettTextPrimary
import com.example.hidrett_app.ui.theme.HidrettTextSecondary
// Adjust this if hidrettFieldColors() lives somewhere else in your theme package.
import com.example.hidrett_app.ui.theme.hidrettFieldColors

private object MainRoutes {
    const val POPULAR_COMMUNITIES = "popular_communities"
    const val START_COMMUNITY = "start_community"
    const val DISCOVER_COMMUNITIES = "discover_communities"
    const val SETTINGS = "settings"
    const val CREATE_POST = "create_post"
    const val PROFILE = "profile"
    fun community(name: String) = "community/$name"
    fun post(id: String) = "post/$id"
}

// Fixed vs. the previous version: positional args here had drifted out of sync with
// Notifications' field order — "3h"/"1d" were landing in `community`, and "p1"/"p2"/"p3"
// were landing in `timestamp`. Using named args below so this can't silently happen again.
fun mockNotifications() = listOf(
    Notifications(
        id = "n1",
        type = NotificationType.Replies,
        title = "quiet_moth_77 replied to your comment",
        preview = "\"Relay-based delivery is basically store-and-forward with...\"",
        community = null,
        timestamp = "12m",
        targetPostId = "p1",
        isRead = true,
        author = "quiet_moth_77"
    ),
    Notifications(
        id = "n2",
        type = NotificationType.Replies,
        title = "cipher_owl_09 replied to your post",
        preview = "\"This is exactly the failure mode I hit too\"",
        community = null,
        timestamp = "1h",
        targetPostId = "p2",
        isRead = true,
        author = "cipher_owl_09"
    ),
    Notifications(
        id = "n3",
        type = NotificationType.Community,
        title = "New pinned post: community guidelines updated",
        preview = null,
        community = "r/privacytech",
        timestamp = "3h",
        targetPostId = "p3",
        isRead = true
    ),
    Notifications(
        id = "n4",
        type = NotificationType.Community,
        title = "Your post was approved by a moderator",
        preview = null,
        community = "r/kotlin",
        timestamp = "1d",
        targetPostId = "p1",
        isRead = false
    )
)

private fun mockFeed() = listOf(
    Post(
        id = "p1",
        communityName = "r/privacytech",
        authorDisplay = "cipher_owl_09",
        timeAgo = "2h",
        title = "Relay-based messaging: what breaks when a relay goes offline?",
        bodyPreview = "Thinking through failure modes for store-and-forward delivery...",
        score = 214,
        commentCount = 38
    ),
    Post(
        id = "p2",
        communityName = "r/anonymity",
        authorDisplay = "quiet_moth_77",
        timeAgo = "5h",
        title = "Why dynamic usernames matter more than people think",
        bodyPreview = "A static handle is still a fingerprint over time...",
        imageUrls = listOf("https://example.com/mock/anon-diagram.png"),
        score = 512,
        commentCount = 96
    ),
    Post(
        id = "p3",
        communityName = "r/kotlin",
        authorDisplay = "null_wren_02",
        timeAgo = "9h",
        title = "Compose state hoisting patterns for chat UIs",
        bodyPreview = "Keeping message lists dumb and pushing status upward...",
        score = 88,
        commentCount = 14
    )
)

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isDrawerOpen by remember { mutableStateOf(false) }
    var isNotificationsOpen by remember { mutableStateOf(false) }
    var selectedNotificationTab by remember { mutableStateOf("All") }
    var notifications by remember { mutableStateOf(mockNotifications()) }
    var feedPosts by remember { mutableStateOf(mockFeed()) }
    var selectedBottomTab by remember { mutableStateOf("Home") }
    var isBottomBarVisible by remember { mutableStateOf(true) }

    val recentlyViewed = remember {
        listOf(
            "CSE2026", "Football Hub", "Gaming Zone", "Programming",
            "Photography", "Animation", "College Life", "Music",
            "Clubs", "Events", "Exams", "Hostels", "Mess Reviews"
        )
    }

    BackHandler(enabled = isNotificationsOpen) { isNotificationsOpen = false }
    BackHandler(enabled = !isNotificationsOpen && isDrawerOpen) { isDrawerOpen = false }
    BackHandler(enabled = !isNotificationsOpen && !isDrawerOpen) { navController.popBackStack() }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val panelWidth = (screenWidth * 0.8f).coerceAtMost(320.dp)

    val visibleNotifications = remember(notifications, selectedNotificationTab) {
        when (selectedNotificationTab) {
            "Replies" -> notifications.filter { it.type == NotificationType.Replies }
            "Community" -> notifications.filter { it.type == NotificationType.Community }
            else -> notifications
        }
    }

    // Hides the bottom bar on scroll-down, shows it on scroll-up. Reacts to raw scroll
    // deltas from the feed's LazyColumn, so it doesn't need that list's state hoisted.
    val bottomBarScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -4f) {
                    isBottomBarVisible = false
                } else if (available.y > 4f) {
                    isBottomBarVisible = true
                }
                return Offset.Zero
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomBarScrollConnection)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HidrettBackground)
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    isDrawerOpen = true
                    isNotificationsOpen = false
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ViewHeadline,
                        contentDescription = "Open menu",
                        tint = HidrettAccent,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .background(HidrettAccent, CircleShape)
                                .size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = "Search Hidrett",
                                tint = HidrettTextPrimary
                            )
                        }
                    },
                    label = { Text("Search Hidrett") },
                    colors = hidrettFieldColors(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            val query = searchQuery.trim()
                            if (query.isNotEmpty()) navController.navigate("search/$query")
                        }
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50.dp)
                )

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(onClick = {
                    isNotificationsOpen = true
                    isDrawerOpen = false
                }) {
                    val hasUnread = notifications.any { !it.isRead }
                    BadgedBox(badge = {
                        if (hasUnread) Badge(containerColor = HidrettAccent)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = HidrettAccent,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Feed(
                posts = feedPosts,
                onVote = { postId, tapped ->
                    feedPosts = feedPosts.map { if (it.id == postId) it.withVote(tapped) else it }
                },
                onToggleSave = { postId ->
                    feedPosts = feedPosts.map {
                        if (it.id == postId) it.copy(isSaved = !it.isSaved) else it
                    }
                },
                onOpenPost = { postId -> navController.navigate(MainRoutes.post(postId)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        // ---------- Bottom tab bar: Home / Create / Profile ----------
        // Uses the already-declared selectedBottomTab state; visibility follows
        // bottomBarScrollConnection above.

        AnimatedVisibility(
            visible = isBottomBarVisible,
            enter = slideInVertically(animationSpec = tween(200)) { fullHeight -> fullHeight } + fadeIn(tween(150)),
            exit = slideOutVertically(animationSpec = tween(200)) { fullHeight -> fullHeight } + fadeOut(tween(150)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            NavigationBar(containerColor = HidrettSurface) {
                NavigationBarItem(
                    selected = selectedBottomTab == "Home",
                    onClick = { selectedBottomTab = "Home" },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = hidrettBottomNavColors()
                )
                NavigationBarItem(
                    selected = selectedBottomTab == "Create",
                    onClick = {
                        selectedBottomTab = "Create"
                        navController.navigate(MainRoutes.CREATE_POST)
                    },
                    icon = { Icon(Icons.Outlined.AddBox, contentDescription = "Create") },
                    label = { Text("Create") },
                    colors = hidrettBottomNavColors()
                )
                NavigationBarItem(
                    selected = selectedBottomTab == "Profile",
                    onClick = {
                        selectedBottomTab = "Profile"
                        navController.navigate(MainRoutes.PROFILE)
                    },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = hidrettBottomNavColors()
                )
            }
        }

        // ---------- Left drawer ----------

        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isDrawerOpen = false }
            )
        }

        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = slideInHorizontally(
                animationSpec = tween(250),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn(tween(150)),
            exit = slideOutHorizontally(
                animationSpec = tween(200),
                targetOffsetX = { fullWidth -> -fullWidth }
            ) + fadeOut(tween(150)),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Surface(
                color = HidrettSurface,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .width(panelWidth)
                    .fillMaxHeight()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    DrawerRow(
                        label = "Popular Communities",
                        leadingIcon = Icons.Outlined.LocalFireDepartment,
                        trailingIcon = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        onClick = {
                            isDrawerOpen = false
                            navController.navigate(MainRoutes.POPULAR_COMMUNITIES)
                        }
                    )
                    HorizontalDivider(color = HidrettTextPrimary.copy(alpha = 0.08f))

                    DrawerRow(
                        label = "Start a community",
                        leadingIcon = Icons.Outlined.AddCircle,
                        onClick = {
                            isDrawerOpen = false
                            navController.navigate(MainRoutes.START_COMMUNITY)
                        }
                    )
                    HorizontalDivider(color = HidrettTextPrimary.copy(alpha = 0.08f))

                    DrawerRow(
                        label = "Discover Communities",
                        leadingIcon = Icons.Outlined.Explore,
                        trailingIcon = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        onClick = {
                            isDrawerOpen = false
                            navController.navigate(MainRoutes.DISCOVER_COMMUNITIES)
                        }
                    )
                    HorizontalDivider(color = HidrettTextPrimary.copy(alpha = 0.08f))

                    Text(
                        text = "RECENTLY VISITED",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = HidrettTextSecondary,
                        modifier = Modifier.padding(horizontal = 22.dp, vertical = 14.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(recentlyViewed, key = { it }) { community ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        isDrawerOpen = false
                                        navController.navigate(MainRoutes.community(community))
                                    }
                                    .padding(horizontal = 22.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                InitialAvatar(text = community, size = 30.dp)
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(text = community, fontSize = 15.sp, color = HidrettTextPrimary)
                            }
                        }
                    }

                    HorizontalDivider(color = HidrettTextPrimary.copy(alpha = 0.08f))

                    DrawerRow(
                        label = "Settings",
                        leadingIcon = Icons.Outlined.Settings,
                        onClick = {
                            isDrawerOpen = false
                            navController.navigate(MainRoutes.SETTINGS)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // ---------- Right notifications panel ----------

        AnimatedVisibility(
            visible = isNotificationsOpen,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isNotificationsOpen = false }
            )
        }

        AnimatedVisibility(
            visible = isNotificationsOpen,
            enter = slideInHorizontally(
                animationSpec = tween(250),
                initialOffsetX = { fullWidth -> fullWidth }
            ) + fadeIn(tween(150)),
            exit = slideOutHorizontally(
                animationSpec = tween(200),
                targetOffsetX = { fullWidth -> fullWidth }
            ) + fadeOut(tween(150)),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Surface(
                color = HidrettSurface,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .width(panelWidth)
                    .fillMaxHeight()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp, 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notifications",
                            color = HidrettTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.weight(1f))
                        if (notifications.any { !it.isRead }) {
                            Text(
                                text = "Mark all read",
                                color = HidrettAccent,
                                fontSize = 13.sp,
                                modifier = Modifier.clickable {
                                    notifications = notifications.map { it.copy(isRead = true) }
                                }
                            )
                        }
                    }

                    HorizontalDivider(color = HidrettTextPrimary.copy(alpha = 0.08f))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        NotificationTab(
                            "All",
                            selected = selectedNotificationTab == "All",
                            onClick = { selectedNotificationTab = "All" },
                            modifier = Modifier.weight(1f)
                        )
                        NotificationTab(
                            "Replies",
                            selected = selectedNotificationTab == "Replies",
                            onClick = { selectedNotificationTab = "Replies" },
                            modifier = Modifier.weight(1f)
                        )
                        NotificationTab(
                            "Community",
                            selected = selectedNotificationTab == "Community",
                            onClick = { selectedNotificationTab = "Community" },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    HorizontalDivider(color = HidrettTextPrimary.copy(alpha = 0.08f))

                    if (visibleNotifications.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Nothing here yet", color = HidrettTextSecondary, fontSize = 14.sp)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                            items(visibleNotifications, key = { it.id }) { notification ->
                                NotificationRow(
                                    notification = notification,
                                    onClick = {
                                        notifications = notifications.map {
                                            if (it.id == notification.id) it.copy(isRead = true) else it
                                        }
                                        isNotificationsOpen = false
                                        navController.navigate(MainRoutes.post(notification.targetPostId))
                                    }
                                )
                                HorizontalDivider(color = HidrettTextPrimary.copy(alpha = 0.06f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun hidrettBottomNavColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = HidrettAccent,
    selectedTextColor = HidrettAccent,
    unselectedIconColor = HidrettTextSecondary,
    unselectedTextColor = HidrettTextSecondary,
    indicatorColor = HidrettBackground
)

@Composable
private fun Feed(
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