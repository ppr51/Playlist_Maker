package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val tracks: ArrayList<Track>
) : RecyclerView.Adapter<TracksViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
//            Log.d("SearchLogTag", "1click on : $position item with trackName==${tracks[position].trackName}");
            onItemClickListener.onTrackClick(tracks[position])
        }
    }

    override fun getItemCount() = tracks.size

    interface OnItemClickListener {
        fun onTrackClick(track: Track)
    }
}