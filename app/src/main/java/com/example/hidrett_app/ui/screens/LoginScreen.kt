package com.example.hidrett_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode.Companion.Plus
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hidrett_app.ui.theme.HidrettAccent
import com.example.hidrett_app.ui.theme.HidrettAccentMuted
import com.example.hidrett_app.ui.theme.HidrettBackground
import com.example.hidrett_app.ui.theme.HidrettError
import com.example.hidrett_app.ui.theme.HidrettSurface
import com.example.hidrett_app.ui.theme.HidrettTextPrimary
import com.example.hidrett_app.ui.theme.HidrettTextSecondary
import com.example.hidrett_app.R
import com.example.hidrett_app.ui.theme.PlusJakartaSans
import com.example.hidrett_app.ui.theme.hidrettFieldColors


object Routes {
    const val WELCOME = "welcome"
    const val FORGOT_PASSWORD = "Forgot Passphrase?"
}

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var handle by rememberSaveable { mutableStateOf("") }

    var passphrase by remember { mutableStateOf("") }

    var handleError by remember { mutableStateOf("") }
    var passphraseError by remember { mutableStateOf("") }
    var passphraseVisible by remember { mutableStateOf(false) }
    var isAuthenticating by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HidrettBackground)
            .padding(horizontal = 32.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.hidrett_icon),
            contentDescription = "Hidrett logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Hidrett",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = PlusJakartaSans,
            color = HidrettTextPrimary,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Real conversations beyond identities",
            fontSize = 15.sp,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            color = HidrettTextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = handle,
            onValueChange = {
                handle = it
                handleError = if (it.isBlank()) "Anonymous handle can't be empty" else ""
            },
            leadingIcon = {
                Icon(Icons.Outlined.Badge, contentDescription = "Anonymous handle")
            },
            isError = handleError.isNotEmpty(),
            supportingText = {
                if (handleError.isNotEmpty()) Text(handleError, color = HidrettError)
            },
            placeholder = {
                Text(
                    text = "Anonymous  Handle",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Normal
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrectEnabled = false
            ),
            colors = hidrettFieldColors(),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = passphrase,
            onValueChange = {
                passphrase = it
                passphraseError = if (it.isBlank()) "Passphrase can't be empty" else ""
            },
            leadingIcon = {
                Icon(Icons.Outlined.VpnKey, contentDescription = "Passphrase")
            },
            trailingIcon = {
                IconButton(onClick = { passphraseVisible = !passphraseVisible }) {
                    Icon(
                        imageVector = if (passphraseVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (passphraseVisible) "Hide passphrase" else "Show passphrase"
                    )
                }
            },
            isError = passphraseError.isNotEmpty(),
            supportingText = {
                if (passphraseError.isNotEmpty()) Text(passphraseError, color = HidrettError)
            },
            placeholder = {
                Text(
                    text = "Passphrase",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Normal
                )
            },
            singleLine = true,
            visualTransformation = if (passphraseVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false
            ),
            colors = hidrettFieldColors(),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            enabled = !isAuthenticating,
            onClick = {
                handleError = if (handle.isBlank()) "Anonymous handle can't be empty" else ""
                passphraseError = if (passphrase.isBlank()) "Passphrase can't be empty" else ""

                if (handle.isNotBlank() && passphrase.isNotBlank()) {
                    isAuthenticating = true
                    // TODO: replace with LoginViewModel.authenticate(handle, passphrase)
                    // Keep auth + navigation decisions out of the composable once real
                    // AuthManager wiring lands — this onClick should just dispatch an intent
                    // to a ViewModel and react to a UiState (Idle/Loading/Error/Success).
                    navController.navigate(Routes.WELCOME)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HidrettAccent,
                contentColor = Color.White,
                disabledContainerColor = HidrettAccent.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            if (isAuthenticating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "ENTER ANONYMOUSLY",
                    fontSize = 16.sp,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        TextButton(onClick = { navController.navigate(Routes.FORGOT_PASSWORD) }) {
            Text(
                text = "Forgot  Passphrase?",
                color = HidrettAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PlusJakartaSans
            )
        }
    }
}
