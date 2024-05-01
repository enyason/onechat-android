package com.enyason.onechat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enyason.onechat.data.ApplicationDataStore
import com.enyason.onechat.data.remote.OneChatApi
import com.enyason.onechat.data.remote.models.LoginRequest
import com.enyason.onechat.ui.navigation.NavigationEvent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val oneChatApi: OneChatApi,
    private val dataStore: ApplicationDataStore
) : ViewModel() {

    private val navigationChannel = Channel<NavigationEvent>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val token = FirebaseMessaging.getInstance().token.await()
            val request = LoginRequest(email, password, token)
            val response = oneChatApi.login(request)
            if (response.success) {
                dataStore.saveUser(response.data.user)
                dataStore.saveToken(response.data.token)
                navigationChannel.send(NavigationEvent.LoggedInNavigationEvent)
            }
        }
    }
}
