package com.example.hidrett_app.ui.screens


import androidx.collection.CircularArray
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.focus.FocusTargetModifierNode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.hidrett_app.ui.components.TokenVisualTransformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope


@Composable
fun RecoverAccountScreen(
    navController: NavController
) {

    var isVerifying by remember {
        mutableStateOf(false)
    }

    var tokenError by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val keyboardController = LocalSoftwareKeyboardController.current

        var activationToken by remember {
            mutableStateOf("")
        }

        var tokenVisible by remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color(0xFF2563EB)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Text(
            text = "HIDRETT",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Secure Dynamic Identity",
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Text(
            text = "Recover Account",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Recover access to your local account using your Activation Token.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        OutlinedTextField(
            value = activationToken,

            onValueChange = { input ->
                val filtered = input
                    .uppercase()
                    .filter { it.isLetterOrDigit() }
                    .take(12)

                activationToken = filtered.chunked(4).joinToString("-")
                tokenError = ""

                if(filtered.length == 12) {
                    keyboardController?.hide()
                }
            },

            modifier = Modifier.fillMaxWidth(),

            leadingIcon =  {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "Activation Token"
                )
            },

            label = {
                Text(
                    "Enter Activation Token"
                )
            },

            placeholder = {
                Text(
                    "XXXX-XXXX-XXXX"
                )
            },

            trailingIcon = {
                IconButton(
                    onClick = {
                        tokenVisible = !tokenVisible
                    }
                ) {
                    Icon(
                        imageVector =
                            if(tokenVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Token Visibility"
                    )
                }
            },

            visualTransformation =
                if(tokenVisible) {
                    VisualTransformation.None
                } else {
                    TokenVisualTransformation()
                },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),

            isError = tokenError.isNotEmpty(),
            supportingText = {
                if(tokenError.isNotEmpty()) {
                    Text(tokenError)
                }
            },

            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F8FF)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Information",
                        tint = Color(0xFF2563EB)
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Recovery Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Your Activation Token is required to recover your local account.",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "You can view it securely anytime from your Hidrett account on our website.",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {
                scope.launch {
                isVerifying = true
                delay(1000)
                isVerifying = false

                val isTokenValid = activationToken == "ABCD-1234-EFGH"

                if(isTokenValid) {
                    navController.navigate("ResetPassword")
                } else {
                    tokenError = "Invalid Activation Token"
                    }
                }
            },

            enabled = !isVerifying &&
                    activationToken.filter { it.isLetterOrDigit() }.length == 12,

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White,

                disabledContainerColor = Color(0xFFDCEBFF),
                disabledContentColor = Color(0xFF6B7280)
            )
        ) {

            if(isVerifying) {
                Text(
                    "Verifying...",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = when {
                        isVerifying -> "VERIFYING..."
                        activationToken.filter { it.isLetterOrDigit() }.length == 12 -> "VERIFY TOKEN"
                        else -> "ENTER ACTIVATION TOKEN"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}