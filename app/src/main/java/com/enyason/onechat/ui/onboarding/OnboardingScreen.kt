package com.enyason.onechat.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.enyason.onechat.ui.Screen


@Composable
fun OnboardingScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {

        // Slogan
        Text(
            text = "Find your people,\nanywhere in the world.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 48.dp) // Adjust top padding as needed
        )

        // Description
        Text(
            text = "Spark conversations, share passions.\nConnect with people who share your interests, all over the globe",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 32.dp
            ) // Adjust paddings as needed
        )

        // Sign up button
        Button(
            onClick = {
                navController.navigate(Screen.Register.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 64.dp), // Adjust paddings as needed
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = "Sign up with email")
        }

        // Login option (optional)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Existing account?")
            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text(text = "Log in")
            }
        }
    }
}
