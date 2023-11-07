package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {

    // Название композиции
    private val trackNameView: TextView = parentView.findViewById(R.id.trackName)
    // Имя исполнителя
    private val artistNameView: TextView = parentView.findViewById(R.id.artistName)
    // Продолжительность трека
    private val trackTimeView: TextView = parentView.findViewById(R.id.trackTime)
    // Ссылка на изображение обложки
    private val artworkUrl100View: ImageView = parentView.findViewById(R.id.albumImage)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        val cornerRadiusOfAlbumImage = 2
        Glide.with(this.itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.vector_placeholder)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusOfAlbumImage))
            .into(artworkUrl100View)
    }
}