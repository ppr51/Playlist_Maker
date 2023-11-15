package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    companion object {
        const val SEARCH_HISTORY_LIST_KEY = "key_search_history_list"
    }

    private var listHistoryTracks = ArrayList<Track>()

    //сохранение в конфиг
    private fun saveHistoryToTheSettings() {
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_LIST_KEY, createJsonFromTrackList(listHistoryTracks))
            .apply()
    }

    // чтение из конфига
    private fun updateHistoryFromSettings() {
        listHistoryTracks.clear()
        val savedJsonStringOfHistoryTracksList = sharedPrefs.getString(SEARCH_HISTORY_LIST_KEY, null)
        if (savedJsonStringOfHistoryTracksList != null)
            listHistoryTracks = createTrackListFromJson(savedJsonStringOfHistoryTracksList)
    }

    //получение сохраненных треков
    fun getHistoryTracks(): ArrayList<Track> {
        updateHistoryFromSettings()
        return listHistoryTracks;
    }

    //передача элемента для сохранения
    fun addNewTrack(track: Track) {
        updateHistoryFromSettings()
        // удаляем если уже есть в истории
        val it = listHistoryTracks.iterator()
        while (it.hasNext()) {
            val tmpTrack = it.next()
            if (tmpTrack.trackId == track.trackId) {
                it.remove()
                break;
            }
        }
        // если треков в истории уже 10, то удалим последний
        if (listHistoryTracks.size == 10)
            listHistoryTracks.removeAt(9)

        listHistoryTracks.add(0, track)
        saveHistoryToTheSettings();
    }

    //очистка истории
    fun clearHistory() {
        listHistoryTracks.clear();
        saveHistoryToTheSettings();
    }

    // конвертер из json в список трэков
    private fun createJsonFromTrackList(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    // конвертер списка трэков в json
    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val arrLstType = object : TypeToken<ArrayList<Track>>() {}
        return Gson().fromJson<ArrayList<Track>>(/* json = */ json, /* classOfT = */ arrLstType)
    }
}