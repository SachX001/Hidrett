package com.example.hidrett_app.ui.screens

import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material3.Icon
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier

    ) {
    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var usernameError by remember {
        mutableStateOf("")
    }

    var passwordError by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(56.dp),

        verticalArrangement = Arrangement.Top,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Text(
            text = "HIDRETT",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(
            modifier = Modifier.height(0.5.dp)
        )

        Text(
            text = "Secure Dynamic Identity",
            fontSize = 16.sp

        )

        Spacer(
            modifier = Modifier.height(45.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError =
                    if(it.isBlank()) {
                        "Username cannot be empty"
                    } else {
                        ""
                    }
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Enter Username"
                )
            },

            isError = usernameError.isNotEmpty(),

            supportingText = {
                if(usernameError.isNotEmpty()) {
                    Text(usernameError)
                }
            },

            label = {
                Text("Enter Username")
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError =
                    if(it.isBlank()) {
                        "Password cannot be empty"
                    } else {
                        ""
                    }
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Enter Password"
                )
            },

            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector =
                            if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                        contentDescription = "Show Password"
                    )
                }
            },

            isError = passwordError.isNotEmpty(),

            supportingText = {
                if(passwordError.isNotEmpty()) {
                    Text(passwordError)
                }
            },

            label =  {
                Text("Enter Password")
            },

            visualTransformation =
                if(passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(
            modifier = Modifier.padding(24.dp)
        )

        Button(
            onClick = {
                usernameError =
                    if(username.isBlank()) {
                        "Username cannot be empty"
                    } else {
                        ""
                    }

                passwordError =
                    if(password.isBlank()) {
                        "Password cannot be empty"
                    } else {
                        ""
                    }

                if(username.isNotBlank() && password.isNotBlank()) {
                    //AuthManager() here ---> Later Stages
                    navController.navigate("Welcome")

                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E3A8A),
                contentColor = Color.White
            )
        ) {
            Text(
            text = "LOGIN",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = {
                navController.navigate("ForgotPassword?")
            }
        ) {
            Text(
                text = "Forgot Password?",
                color = Color(0xFF1E3A8A),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Hidrett_AppTheme {
        LoginScreen()
    }
}
*/