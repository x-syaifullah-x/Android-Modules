package com.umn.iptv.activities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.umn.iptv.R
import com.umn.iptv.databinding.PlaylistItemBinding

class PlaylistRecyclerViewAdapter : RecyclerView.Adapter<PlaylistRecyclerViewViewHolder>() {

    private val _items = mutableListOf<PlaylistModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(data: List<PlaylistModel>) {
        _items.clear()
        _items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistRecyclerViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return PlaylistRecyclerViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    override fun onBindViewHolder(holder: PlaylistRecyclerViewViewHolder, position: Int) {
        val model = _items[position]
        val playlistItemBinding = PlaylistItemBinding.bind(holder.view)
        playlistItemBinding.root.setOnClickListener {
            Toast.makeText(it.context, "Coming soon",Toast.LENGTH_SHORT).show()
        }
        playlistItemBinding.tvName.text = model.name
        playlistItemBinding.tvUrl.text = model.url
    }
}