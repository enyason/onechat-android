package com.enyason.onechat.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enyason.onechat.data.ApplicationDataStore
import com.enyason.onechat.data.remote.OneChatApi
import com.enyason.onechat.data.remote.models.LoginRequest
import com.enyason.onechat.data.remote.models.RegistrationRequest
import com.enyason.onechat.ui.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val oneChatApi: OneChatApi,
    private val dataStore: ApplicationDataStore
) : ViewModel() {

    private val navigationChannel = Channel<NavigationEvent>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    fun register(email: String, password: String, username: String, fullName: String) {
        viewModelScope.launch {
            val request = RegistrationRequest(email, fullName, username, password)
            val response = oneChatApi.register(request)
            println(response)
            if (response.success) {
                navigationChannel.send(NavigationEvent.RegisterNavigationEvent)
            }
        }
    }
}
