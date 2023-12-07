package id.xxx.example.chat.data.model

data class ChatModel(
    val id: String,
    val members: List<String>,
    val timeMillis: Long?,
    val lastMessage: String?,
    val fromUID: String,
    val toUID: String
)