package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val bBack = findViewById<Button>(R.id.back_from_player_to_search_button)
        bBack.setOnClickListener {
            finish()
        }
        // Изображение обложки
        val artworkUrl512View = findViewById<ImageView>(R.id.playerAlbumImage)
        // Название композиции
        val trackNameView = findViewById<TextView>(R.id.text_trackName)
        // Имя исполнителя
        val artistNameView = findViewById<TextView>(R.id.text_artistName)
        //Прогресс воспроизведения
        val listenProgressTimeView = findViewById<TextView>(R.id.text_trackListenTime)
        // Длительность трека
        val trackTimeView = findViewById<TextView>(R.id.text_trackLength)
        // Название альбома
        val albumNameView = findViewById<TextView>(R.id.text_album)
        val labelAlbumNameView = findViewById<TextView>(R.id.label_album)
        // Год
        val yearView = findViewById<TextView>(R.id.text_year)
        // Жанр
        val genreView = findViewById<TextView>(R.id.text_genre)
        // Страна
        val countryView = findViewById<TextView>(R.id.text_country)

        // получаем данные о трэке из итента
        val jsonStr = intent.getStringExtra(Track::class.simpleName)
        val track = Gson().fromJson(jsonStr, Track::class.java)

        val cornerRadiusOfAlbumImage = 8
        Glide.with(this.applicationContext)
            .load(convertImageUrlToWideImgUrl(track.artworkUrl100))
            .placeholder(R.drawable.vector_placeholder)
            .transform(RoundedCorners(cornerRadiusOfAlbumImage))
            .into(artworkUrl512View)

        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        if (track.trackTime != null)
            trackTimeView.text = getTrackTimeFromMillis(track.trackTime)

        // скрываем информацию об альбоме, если ее нет
        val isCollectionExist = track.collectionName!=null && track.collectionName.isNotEmpty()
        albumNameView.isVisible = isCollectionExist
        labelAlbumNameView.isVisible = isCollectionExist
        if(isCollectionExist)
            albumNameView.text = track.collectionName

        yearView.text = getYearFromDateString(track.releaseDate)
        genreView.text = track.primaryGenreName
        countryView.text = track.country
        listenProgressTimeView.text = "00:00"
    }

}