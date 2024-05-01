package com.enyason.onechat.ui.registration


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enyason.onechat.ui.Screen
import com.enyason.onechat.ui.login.LoginViewModel
import com.enyason.onechat.ui.navigation.NavigationEvent
import com.enyason.onechat.ui.navigation.observeAsEvents

@Composable
fun RegistrationScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<RegistrationViewModel>()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }

    observeAsEvents(viewModel.navigationFlow) { event ->
        if (event is NavigationEvent.RegisterNavigationEvent) {
            navController.navigate(Screen.Login.route)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App name or logo (optional)
        Text(text = "OneChat", style = MaterialTheme.typography.headlineLarge)

        // Welcome message
        Text(
            text = "Get chatting with friends and family today\nby signing up for OneChat!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp) // Optional padding
        )

        // Full name text field
        OutlinedTextField(
            value = fullname,
            onValueChange = { fullname = it },
            label = { Text("Your full name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Email text field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Your email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Username text field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password text field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        // Create account button
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Optional padding
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = "Create an account")
        }
    }
}
