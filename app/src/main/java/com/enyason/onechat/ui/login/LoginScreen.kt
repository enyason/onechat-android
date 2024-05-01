package com.enyason.onechat.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enyason.onechat.ui.Screen
import com.enyason.onechat.ui.navigation.NavigationEvent
import com.enyason.onechat.ui.navigation.observeAsEvents


@Composable
fun LogInScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel = hiltViewModel<LoginViewModel>()

    observeAsEvents(viewModel.navigationFlow) { event ->
        if (event is NavigationEvent.LoggedInNavigationEvent) {
            navController.navigate(Screen.Rooms.route)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo or app name (optional)
        Text(text = "OneChat", style = MaterialTheme.typography.headlineLarge)

        // Welcome message
        Text(text = "Welcome back!", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Sign in using your email ID", style = MaterialTheme.typography.headlineSmall)

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Your email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
        )

        // Login button
        OutlinedButton(
            onClick = {
                viewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Log in")
        }

        TextButton(
            onClick = {

            },
        ) {
            Text(text = "Forgot password?")
        }
    }
}

