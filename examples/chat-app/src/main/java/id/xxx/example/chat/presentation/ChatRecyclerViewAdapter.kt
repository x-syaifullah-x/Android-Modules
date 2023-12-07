package id.xxx.example.chat.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.xxx.example.chat.R
import id.xxx.example.chat.data.model.ChatModel
import id.xxx.example.chat.databinding.ChatItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRecyclerViewAdapter : RecyclerView.Adapter<ChatRecyclerViewViewHolder>() {

    private val _items = mutableListOf<ChatModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(data: List<ChatModel>) {
        _items.clear()
        _items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRecyclerViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ChatRecyclerViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    override fun onBindViewHolder(holder: ChatRecyclerViewViewHolder, position: Int) {
        val model = _items[position]
        println("onBindViewHolder: $model")
        val chatItemBinding = ChatItemBinding.bind(holder.view)
        chatItemBinding.tvTitle.text = model.toUID
        chatItemBinding.tvSubTitle.text = model.lastMessage
        val timeMillis = model.timeMillis
        if (timeMillis != null) {
            chatItemBinding.tvDate.text =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(Date(timeMillis))
        }
    }
}