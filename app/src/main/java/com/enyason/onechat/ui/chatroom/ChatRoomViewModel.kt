package com.enyason.onechat.ui.chatroom

import android.net.http.X509TrustManagerExtensions
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enyason.onechat.data.ApplicationDataStore
import com.enyason.onechat.data.remote.OneChatApi
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.network.tls.TLSConfigBuilder
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import java.security.cert.X509Certificate
import java.util.*
import javax.inject.Inject
import javax.net.ssl.X509TrustManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val oneChatApi: OneChatApi,
    private val gson: Gson,
    private val dataStore: ApplicationDataStore
) : ViewModel(), WebSocketListener {


    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    private val client = HttpClient(CIO) {
        engine {
            https {
                serverName = "onechat-api.azurewebsites.net"
                trustManager = MyTrustManager(this)
            }
        }
        install(WebSockets)
    }

    private var wsClientSession: DefaultClientWebSocketSession? = null

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Coroutine exeception = ${throwable.message}")
    }


    fun sendMessageToRoom(text: String) {
        viewModelScope.launch {
            dataStore.getUser()?.let { user ->
                val message = WsDto(data = WsMessage(message = text, sender = user.username))
                val jsonString = gson.toJson(message, WsDto::class.java)
                wsClientSession?.send(Frame.Text(jsonString))
            }

        }
    }

    fun wsConnect(roomId: String, listener: WebSocketListener = this) {
        viewModelScope.launch {

            val token = dataStore.getToken()?.accessToken
            client.wss(
                urlString = "wss://onechat-api.azurewebsites.net/ws/chat/$roomId?token=$token"
            ) {
                listener.onConnected()
                wsClientSession = this
                try {
                    for (frame in incoming) {
                        println("ws frame = $frame")
                        if (frame is Frame.Text) {
                            val message = gson.fromJson(frame.readText(), WsDto::class.java)
                            println("ws frame text = $message")
                            if (message.event_type == "message") {
                                listener.onMessage(message)
                            }

                        }
                    }
                } catch (e: Exception) {
                    println("Web socket exeception = ${e.message}")
                    listener.onDisconnected()
                    wsClientSession = null
                }
            }
        }
    }


    fun getMessages(roomId: String) {
        viewModelScope.launch {
            try {
                val currentUser = dataStore.getUser()
                val response = oneChatApi.getRoomMessages(roomId)
                val messages = response.data.map { message ->
                    ChatMessage(
                        id = message.id,
                        text = message.content,
                        participant = message.author,
                        isUserMessage = message.author == currentUser?.username
                    )
                }

                _chatMessages.emit(messages)
            } catch (error: Exception) {
                println("API Error: ${error.message}")

            }
        }
    }

    override fun onConnected() {
        println("web socket is connected...")
    }

    override fun onMessage(message: WsDto) {
        val currentUser = dataStore.getUser()
        val newValue = listOf(
            ChatMessage(
                id = message.data.message_id,
                text = message.data.message,
                participant = message.data.sender,
                isUserMessage = message.data.sender == currentUser?.username
            )
        )
        viewModelScope.launch {
            _chatMessages.update { previousValue -> previousValue + newValue }
        }
        println("incoming message... $message")
    }

    override fun onDisconnected() {
        println("web socket is disconnected...")
    }
}


data class ChatMessage(
    val id: String,
    var text: String,
    val participant: String,
    val isUserMessage: Boolean,
    var isPending: Boolean = false,

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
    val message: String = "Pushing to room",
    val data: WsMessage
)

data class WsMessage(
    val message_id: String = UUID.randomUUID().toString(),
    val message: String, val sender: String
)

class MyTrustManager(private val config: TLSConfigBuilder) : X509TrustManager {
    private val delegate = config.build().trustManager
    private val extensions = X509TrustManagerExtensions(delegate)

    override fun checkClientTrusted(certificates: Array<out X509Certificate>?, authType: String?) {
        extensions.checkServerTrusted(certificates, authType, config.serverName)
    }

    override fun checkServerTrusted(certificates: Array<out X509Certificate>?, authType: String?) {
        delegate.checkServerTrusted(certificates, authType)
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = delegate.acceptedIssuers
}
