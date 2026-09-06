package com.example.hidrett_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hidrett_app.ui.theme.HidrettAccent
import com.example.hidrett_app.ui.theme.HidrettAccentMuted
import com.example.hidrett_app.ui.theme.HidrettBackground
import com.example.hidrett_app.ui.theme.HidrettError
import com.example.hidrett_app.ui.theme.HidrettSurface
import com.example.hidrett_app.ui.theme.HidrettTextPrimary
import com.example.hidrett_app.ui.theme.HidrettTextSecondary
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.hidrett_app.R

@Composable
fun WelcomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    holdMillis: Long? = null,
    onRevealFinished: () -> Unit = {},
) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { revealed = true }

    val transition = updateTransition(targetState = revealed, label = "WelcomeReveal")
    val easing: Easing = LinearOutSlowInEasing

    val logoScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500, easing = easing) },
        label = "logoScale",
    ) { if (it) 1f else 0.75f }

    val logoAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 400, easing = easing) },
        label = "logoAlpha",
    ) { if (it) 1f else 0f }

    val titleAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 400, delayMillis = 90, easing = easing) },
        label = "titleAlpha",
    ) { if (it) 1f else 0f }

    val titleOffsetY by transition.animateDp(
        transitionSpec = { tween(durationMillis = 450, delayMillis = 90, easing = easing) },
        label = "titleOffsetY",
    ) { if (it) 0.dp else 12.dp }

    val taglineAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 400, delayMillis = 180, easing = easing) },
        label = "taglineAlpha",
    ) { if (it) 1f else 0f }

    BackHandler {
        navController.popBackStack()
    }

    if (holdMillis != null) {
        LaunchedEffect(revealed) {
            if (revealed) {
                delay(500 + holdMillis)
                onRevealFinished()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = HidrettBackground),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.hidrett_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .graphicsLayer {
                        scaleX = logoScale
                        scaleY = logoScale
                        alpha = logoAlpha
                    },
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Hidrett",
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer {
                    alpha = titleAlpha
                    translationY = titleOffsetY.toPx()
                },
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "real conversations beyond identities",
                color = Color(0xFFA0A0A5),
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(taglineAlpha),
            )
        }
    }
}