package com.example.playlistmaker

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Track(
    val trackName: String,        // Название композиции
    val artistName: String,       // Имя исполнителя
    @SerializedName("trackTimeMillis") var trackTime: String, // Продолжительность трека
    val artworkUrl100: String,    // Ссылка на изображение обложки
    val trackId: Int,             // Уникальный идентификатор трэка
    val collectionName: String,   // Название альбома
    val releaseDate: String,      // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String           // Страна исполнителя
)

fun convertImageUrlToWideImgUrl(imgUrl: String): String {
    var res = imgUrl
    res = res.replaceAfterLast(
        delimiter = '/',
        replacement = "512x512bb.jpg"
    )
    return res
}

fun getTrackTimeFromMillis(trackTimeMillisStr: String): String {
    var res: String = ""
    if (trackTimeMillisStr != null) {
        var longNum: Long? = trackTimeMillisStr.trim().toLongOrNull()
        if (longNum == null) {
            res = trackTimeMillisStr
        } else {
            res = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(longNum)
        }
    }
    return res
}

@SuppressLint("SimpleDateFormat")
fun getYearFromDateString(dateStr: String): String {
    //1999-02-02T08:00:00Z
    var date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateStr)
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
}
