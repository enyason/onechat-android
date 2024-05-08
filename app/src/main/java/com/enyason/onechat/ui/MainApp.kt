package com.enyason.onechat.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enyason.onechat.data.ApplicationDataStore
import com.enyason.onechat.ui.chatroom.ChatRoomScreen
import com.enyason.onechat.ui.login.LogInScreen
import com.enyason.onechat.ui.onboarding.OnboardingScreen
import com.enyason.onechat.ui.registration.RegistrationScreen
import com.enyason.onechat.ui.rooms.RoomsScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp(dataStore: ApplicationDataStore) {
    val navController = rememberNavController()
    Scaffold(
    ) {
        // Navigation destinations
        val initialRoute = when {
            dataStore.getToken()?.accessToken != null -> Screen.Rooms.route
            else -> Screen.Onboarding.route
        }

        NavHost(navController = navController, startDestination = initialRoute) {
            composable(route = Screen.Onboarding.route) { OnboardingScreen(navController) }
            composable(route = Screen.Login.route) { LogInScreen(navController) }
            composable(route = Screen.Register.route) { RegistrationScreen(navController) }
            composable(route = Screen.Rooms.route) { RoomsScreen(navController) }
            composable(route = "room_chat/{roomId}") { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId")
                ChatRoomScreen(navController, roomId)
            }
        }
    }
}


sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Rooms : Screen("rooms")
    data class RoomChat(val roomId: String) : Screen("room_chat/$roomId")
}
