package com.example.hidrett_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
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

/**
 * Centralized routes to avoid typo-prone string literals scattered across composables.
 * (Move this to its own Routes.kt once the nav graph grows.)
 */
object Routes {
    const val WELCOME = "welcome"
    const val FORGOT_PASSWORD = "Forgot Passphrase?"
}

// Dark, low-signature palette — deliberately avoids anything "social app" bright/friendly,
// leans toward the "encrypted terminal" register instead.
private val HidrettBackground = Color(0xFF0B0B0F)
private val HidrettSurface = Color(0xFF16161D)
private val HidrettAccent = Color(0xFF7C5CFC)      // muted violet, not a "trustworthy blue"
private val HidrettAccentMuted = Color(0xFF9B8CFF)
private val HidrettTextPrimary = Color(0xFFEDEDF2)
private val HidrettTextSecondary = Color(0xFF8A8A99)
private val HidrettError = Color(0xFFFF6B6B)

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Handle survives rotation — it's not sensitive on its own.
    var handle by rememberSaveable { mutableStateOf("") }

    // Passphrase intentionally does NOT use rememberSaveable: we don't want plaintext
    // credentials surviving process death in the saved-instance-state bundle, even briefly.
    var passphrase by remember { mutableStateOf("") }

    var handleError by remember { mutableStateOf("") }
    var passphraseError by remember { mutableStateOf("") }
    var passphraseVisible by remember { mutableStateOf(false) }
    var isAuthenticating by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HidrettBackground)
            .padding(horizontal = 32.dp, vertical = 56.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = null,
            tint = HidrettAccent,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "HIDRETT",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = HidrettTextPrimary,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Secure Dynamic Identity",
            fontSize = 15.sp,
            color = HidrettTextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "No real names. No phone numbers. Ever.",
            fontSize = 12.sp,
            color = HidrettAccentMuted
        )

        Spacer(modifier = Modifier.height(48.dp))

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
            label = { Text("Anonymous Handle") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrectEnabled = false
            ),
            colors = hidrettFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            label = { Text("Passphrase") },
            singleLine = true,
            visualTransformation = if (passphraseVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false
            ),
            colors = hidrettFieldColors(),
            modifier = Modifier.fillMaxWidth()
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
            )
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
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        TextButton(onClick = { navController.navigate(Routes.FORGOT_PASSWORD) }) {
            Text(
                text = "Forgot Passphrase?",
                color = HidrettAccentMuted,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun hidrettFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = HidrettTextPrimary,
    unfocusedTextColor = HidrettTextPrimary,
    focusedBorderColor = HidrettAccent,
    unfocusedBorderColor = HidrettTextSecondary,
    focusedLabelColor = HidrettAccentMuted,
    unfocusedLabelColor = HidrettTextSecondary,
    cursorColor = HidrettAccent,
    focusedLeadingIconColor = HidrettAccentMuted,
    unfocusedLeadingIconColor = HidrettTextSecondary,
    errorBorderColor = HidrettError,
    errorLabelColor = HidrettError
)