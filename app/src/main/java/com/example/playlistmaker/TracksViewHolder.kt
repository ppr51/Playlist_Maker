package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {

    private val trackNameView: TextView      // Название композиции
    private val artistNameView: TextView     // Имя исполнителя
    private val trackTimeView: TextView      // Продолжительность трека
    private val artworkUrl100View: ImageView // Ссылка на изображение обложки
    init {
        trackNameView = parentView.findViewById(R.id.trackName)
        artistNameView = parentView.findViewById(R.id.artistName)
        trackTimeView = parentView.findViewById(R.id.trackTime)
        artworkUrl100View = parentView.findViewById(R.id.albumImage)
    }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        Glide.with(this.itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.vector_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkUrl100View)
    }
}