package id.xxx.example.chat.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.xxx.example.chat.data.model.Result
import id.xxx.example.chat.data.repository.ChatRepository
import id.xxx.example.chat.databinding.ChatActivityBinding
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private val activityBinding by lazy {
        ChatActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityBinding.root)

        val rvChat = activityBinding.rvChat
        rvChat.layoutManager = LinearLayoutManager(this)
        val adapter = ChatRecyclerViewAdapter()
        rvChat.adapter = adapter

        val chatRepo = ChatRepository.getInstance()
        lifecycleScope.launch {
            val uid = "uid_1"

//            chatRepo.sendMessage(
//                fromUid = uid,
//                toUid = "uid_5",
//                model = MessageModel(message = "hay", senderID = uid)
//            ).collect()

            val toast = Toast.makeText(this@ChatActivity, null, Toast.LENGTH_LONG)
            chatRepo.getChats(uid).collect {
                when (it) {
                    is Result.Loading -> {
                        toast.setText("Loading ...")
                        toast.show()
                    }

                    is Result.Success -> {
                        toast.cancel()
                        adapter.setItem(it.value)
                    }

                    is Result.Error -> {
                        toast.setText(it.value.localizedMessage)
                        toast.show()
                    }
                }
            }
        }
    }
}