package com.enyason.onechat.ui.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enyason.onechat.data.remote.OneChatApi
import com.enyason.onechat.data.remote.models.Room
import com.enyason.onechat.ui.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val oneChatApi: OneChatApi
) : ViewModel() {

    private val navigationChannel = Channel<NavigationEvent>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    fun fetchRooms() {
        viewModelScope.launch {
            val response = oneChatApi.getRooms()
            if (response.success) {
                _rooms.emit(response.data)
            }
        }
    }
}
