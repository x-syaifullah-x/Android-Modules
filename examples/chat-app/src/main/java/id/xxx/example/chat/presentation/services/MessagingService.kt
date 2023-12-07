package id.xxx.example.chat.presentation.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        println("onMessageReceived: ${message.data}")
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()

        println("onDeletedMessages")
    }
}