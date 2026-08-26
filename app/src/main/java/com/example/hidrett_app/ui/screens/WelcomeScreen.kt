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

@Composable
fun WelcomeScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {
        delay(1000.milliseconds)

        navController.navigate("MainScreen") {
            popUpTo("Welcome") {
                inclusive = true;
            }
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HidrettBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = null,
            tint = HidrettAccent,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "HIDRETT",
            fontSize = 40.sp,
            letterSpacing = 4.sp,
            fontWeight = FontWeight.Bold,
            color = HidrettTextPrimary
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Text(
            text = "A Secure Dynamic Identity Platform",
            color = HidrettTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
