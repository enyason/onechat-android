package com.enyason.onechat

import com.enyason.onechat.extensions.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneChatMessagingService : FirebaseMessagingService() {


    override fun onNewToken(p0: String) {}

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        println("incoming_notification = $remoteMessage")
        println("incoming_notification data = ${remoteMessage.data}")
        println("incoming_notification = ${remoteMessage.notification}")
        if (remoteMessage.data.isNotEmpty()) {

            when (remoteMessage.data["notificationType"]) {
                MESSAGE_NOTIFICATION_TYPE -> {

                    val type = remoteMessage.data["type"]
                    val sender = remoteMessage.data["fromUsername"]

                    val title = "New room message"

                    val body = remoteMessage.data["message"]


                    val notification = NotificationItem(
                        title, body.toString(), ROOM_MESSAGE_CHANNEL_ID, ROOM_MESSAGE_CHANNEL_NAME
                    )

                    sendNotification(notification)

                }

            }


        }

    }

    companion object {
        const val ROOM_MESSAGE_CHANNEL_ID = "one_chat_room_message"
        const val ROOM_MESSAGE_CHANNEL_NAME = "Room Message"
        const val MESSAGE_NOTIFICATION_TYPE = "ChatMessage"
    }
}
