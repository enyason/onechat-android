package com.enyason.onechat.ui.chatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enyason.onechat.data.ApplicationDataStore
import com.enyason.onechat.data.remote.OneChatApi
import com.enyason.onechat.data.remote.models.LoginRequest
import com.enyason.onechat.data.remote.models.Message
import com.enyason.onechat.data.remote.models.Room
import com.enyason.onechat.ui.navigation.NavigationEvent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.wss
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val oneChatApi: OneChatApi,
    private val dataStore: ApplicationDataStore
) : ViewModel(), WebSocketListener {


    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    private val client = HttpClient { install(WebSockets) }


    fun wsConnect(roomId: String, listener: WebSocketListener = this) {
        viewModelScope.launch {
            val wsUrl = "onechat-api.azurewebsites.net/ws/chat/$roomId"
            client.wss(wsUrl) {
                listener.onConnected()
                try {
                    for (frame in incoming) {
                        println("ws frame = $frame")
                        if (frame is Frame.Text) {
                            println("ws frame text = $frame")
//                            listener.onMessage(frame.readText())
                        }
                    }
                } catch (e: Exception) {
                    listener.onDisconnected()
                }
            }
        }
    }


    fun getMessages(roomId: String) {
        viewModelScope.launch {
            val currentUser = dataStore.getUser()
            val response = oneChatApi.getRoomMessages(roomId)
            val messages = response.data.map { message ->
                ChatMessage(
                    id = message.id,
                    text = message.content,
                    participant = if (currentUser?.username == message.author) Participant.THIS_USER else Participant.OTHER_USER
                )
            }

            _chatMessages.emit(messages)
        }
    }

    override fun onConnected() {
        println("web socket is connected...")
    }

    override fun onMessage(message: WsDto) {
        println("incoming message... $message")
    }

    override fun onDisconnected() {
        println("web socket is disconnected...")
    }
}


data class ChatMessage(
    val id: String,
    var text: String,
    val participant: Participant,
    var isPending: Boolean = false
)

enum class Participant {
    THIS_USER, OTHER_USER
}


interface WebSocketListener {
    fun onConnected()
    fun onMessage(message: WsDto)
    fun onDisconnected()
}


data class WsDto(
    val event_type: String = "message",
    val data: WsMessage,
    val sender_id: String

)

data class WsMessage(val message: String)
