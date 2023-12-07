package id.xxx.example.chat.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import id.xxx.example.chat.data.model.ChatModel
import id.xxx.example.chat.data.model.MessageModel
import id.xxx.example.chat.data.model.Result
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest

class ChatRepository private constructor(private val remoteDataSource: FirebaseFirestore) {

    companion object {

        private const val COLLECTION_PATH_CHATS = "chats"
        private const val COLLECTION_PATH_CONTENT = "content"
        private const val FIELD_CHAT_LAST_MESSAGE = "last_message"
        private const val FIELD_CHAT_MEMBERS = "members"
        private const val FIELD_CHAT_TIME_MILLIS = "time_millis"
        private const val FIELD_CHAT_CONTENT_MESSAGE = "message"
        private const val FIELD_CHAT_CONTENT_SENDER_ID = "sender_id"
        private const val FIELD_CHAT_CONTENT_TIME_MILLIS = "time_millis"

        @Volatile
        private var INSTANCE: ChatRepository? = null

        fun getInstance() = INSTANCE
            ?: synchronized(this) {
                INSTANCE ?: ChatRepository(FirebaseFirestore.getInstance())
                    .also { INSTANCE = it }
            }
    }

    /**
     * [chatID] == [ChatModel.id]
     */
    suspend fun getMessages(chatID: String): List<MessageModel> {
        val querySnapshot = remoteDataSource
            .collection(COLLECTION_PATH_CHATS)
            .document(chatID)
            .collection(COLLECTION_PATH_CONTENT)
            .get()
            .await()
        val results = mutableListOf<MessageModel>()
        querySnapshot.forEach { queryDocumentSnapshot ->
            val messageId = queryDocumentSnapshot.id
            val senderId = queryDocumentSnapshot.data[FIELD_CHAT_CONTENT_SENDER_ID] as? String
            val message = queryDocumentSnapshot.data[FIELD_CHAT_CONTENT_MESSAGE] as? String
            results.add(
                MessageModel(
                    id = messageId,
                    senderID = senderId
                        ?: throw IllegalArgumentException("sender_id is null"),
                    message = message ?: ""
                )
            )
        }
        return results
    }

    suspend fun getChats(uid: String) = flow {
        emit(Result.Loading())
        try {
            val chats = remoteDataSource
                .collection(COLLECTION_PATH_CHATS)
                .whereArrayContains(FIELD_CHAT_MEMBERS, uid)
                .get()
                .await()
                .sortedByDescending { it.data[FIELD_CHAT_TIME_MILLIS] as Long }
            val results = mutableListOf<ChatModel>()
            chats.forEach { chat ->
                val chatData = chat.data

                @Suppress("UNCHECKED_CAST")
                val members = chatData[FIELD_CHAT_MEMBERS] as List<String>
                val lastMessage = chatData[FIELD_CHAT_LAST_MESSAGE] as? String
                val timeMillis = chatData[FIELD_CHAT_TIME_MILLIS] as? Long
                val model = ChatModel(
                    id = chat.id,
                    members = members,
                    timeMillis = timeMillis,
                    lastMessage = lastMessage,
                    fromUID = members.firstOrNull { it == uid }
                        ?: throw IllegalArgumentException(),
                    toUID = members.firstOrNull { it != uid } ?: uid
                )
                results.add(model)
            }
            emit(Result.Success(results))
        } catch (e: Throwable) {
            emit(Result.Error(e))
        }
    }

    suspend fun sendMessage(
        uidForm: String,
        uidTo: String,
        model: MessageModel? = null
    ) = flow {
        emit(Result.Loading())
        val chatID = createOrUpdateChatRoom(uidForm, uidTo, lastMessage = model?.message)
        if (chatID != null) {
            if (model != null) {
                try {
                    val data = mutableMapOf<String, Any>()
                    data[FIELD_CHAT_CONTENT_MESSAGE] = model.message
                    data[FIELD_CHAT_CONTENT_SENDER_ID] = model.senderID
                    data[FIELD_CHAT_CONTENT_TIME_MILLIS] = System.currentTimeMillis()
                    val result = remoteDataSource
                        .collection(COLLECTION_PATH_CHATS)
                        .document(chatID)
                        .collection(COLLECTION_PATH_CONTENT)
                        .document()
                        .set(data)
                        .await()
                    emit(Result.Success(result))
                } catch (e: Throwable) {
                    emit(Result.Error(e))
                }
            } else {
                emit(Result.Success(null))
            }
        } else {
            emit(Result.Error(Throwable("Failed to create a chat room")))
        }
    }

    private suspend fun createOrUpdateChatRoom(vararg uid: String, lastMessage: String?) =
        try {
            val chatID = generateChatID(*uid)
            val data = mapOf(
                FIELD_CHAT_MEMBERS to uid.toList(),
                FIELD_CHAT_TIME_MILLIS to System.currentTimeMillis(),
                FIELD_CHAT_LAST_MESSAGE to lastMessage
            )
            remoteDataSource.collection(COLLECTION_PATH_CHATS)
                .document(chatID)
                .set(data)
                .await()
            chatID
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }

    private fun generateChatID(vararg uid: String): String {
        val combinedUID = uid
            .sorted()
            .joinToString("-")
        val hashedBytes = MessageDigest.getInstance("MD5")
            .digest(combinedUID.toByteArray())
        return hashedBytes.joinToString("") { "%02x".format(it) }
    }
}