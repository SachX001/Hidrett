package com.example.hidrett_app.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewHeadline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hidrett_app.ui.components.DrawerRow
import com.example.hidrett_app.ui.components.InitialAvatar
import com.example.hidrett_app.ui.theme.HidrettAccent
import com.example.hidrett_app.ui.theme.HidrettBackground
import com.example.hidrett_app.ui.theme.HidrettSurface
import com.example.hidrett_app.ui.theme.HidrettTextPrimary
import com.example.hidrett_app.ui.theme.HidrettTextSecondary
import com.example.hidrett_app.ui.theme.hidrettFieldColors

private object MainRoutes {
    const val POPULAR_COMMUNITIES = "popular_communities"
    const val START_COMMUNITY = "start_community"
    const val DISCOVER_COMMUNITIES = "discover_communities"
    const val SETTINGS = "settings"
    fun community(name: String) = "community/$name"
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isDrawerOpen by remember { mutableStateOf(false) }
    var isNotificationsOpen by remember {mutableStateOf(false) }

    val recentlyViewed = remember {
        listOf(
            "CSE2026", "Football Hub", "Gaming Zone", "Programming",
            "Photography", "Animation", "College Life", "Music",
            "Clubs", "Events", "Exams", "Hostels", "Mess Reviews"
        )
    }

    BackHandler(enabled = isDrawerOpen) { isDrawerOpen = false }
    BackHandler(enabled = !isDrawerOpen) { navController.popBackStack() }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = (screenWidth * 0.8f).coerceAtMost(320.dp)

    Box(modifier = Modifier.fillMaxSize()) {

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
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = HidrettAccent,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

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
                    .width(drawerWidth)
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
                                Text(
                                    text = community,
                                    fontSize = 15.sp,
                                    color = HidrettTextPrimary
                                )
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
                    .width(drawerWidth)
                    .fillMaxHeight()
            ) {

            }
        }
    }
}
