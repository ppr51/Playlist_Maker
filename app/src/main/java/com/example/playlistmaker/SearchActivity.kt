package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TracksAdapter.OnItemClickListener {

    private var textInSearchLine: String = ""
    private lateinit var searchingLine: EditText
    private lateinit var bBack: Button
    private lateinit var clearButton: Button
    private lateinit var rvTracksContainer: RecyclerView
    private lateinit var placeholderNothingFound: LinearLayout
    private lateinit var placeholderNetworkProblem: LinearLayout
    private lateinit var searchUpdateButton: Button
    private lateinit var laySearchResults: LinearLayout
    private lateinit var tvHistoryTitle: TextView
    private lateinit var buttonclearSearchHistory: Button

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHist: SearchHistory

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val listTracks = ArrayList<Track>()

    private lateinit var adapterTracks: TracksAdapter

    private companion object {
        const val TEXT_IN_SEARCH_LINE = "TEXT_IN_SEARCH_LINE"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_IN_SEARCH_LINE, textInSearchLine)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textInSearchLine = savedInstanceState.getString(TEXT_IN_SEARCH_LINE, "")
        searchingLine.setText(textInSearchLine)
    }

    fun showViewByFlag(
        isVisibleResultsList: Boolean,
        isVisibleNothingFound: Boolean,
        isVisibleNetworkProblem: Boolean
    ) {
        laySearchResults.isVisible = isVisibleResultsList
        placeholderNothingFound.isVisible = isVisibleNothingFound
        placeholderNetworkProblem.isVisible = isVisibleNetworkProblem
    }

    fun setHistoryVisibility(isVisibleHistory: Boolean) {
        laySearchResults.isVisible = isVisibleHistory
        tvHistoryTitle.isVisible = isVisibleHistory
        buttonclearSearchHistory.isVisible = isVisibleHistory
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefs = (applicationContext as App).sharedPrefs

        searchingLine = findViewById<EditText>(R.id.searchingLine)
        bBack = findViewById<Button>(R.id.back_from_search_to_main_button)
        clearButton = findViewById<Button>(R.id.clearButton)
        rvTracksContainer = findViewById<RecyclerView>(R.id.tracksContainerRecyclerView)
        placeholderNothingFound = findViewById<LinearLayout>(R.id.nothing_found_placeholder)
        placeholderNetworkProblem = findViewById<LinearLayout>(R.id.network_problem_placeholder)
        searchUpdateButton = findViewById<Button>(R.id.search_update_button)

        adapterTracks = TracksAdapter(this, listTracks)

        laySearchResults = findViewById<LinearLayout>(R.id.search_results_layout)
        tvHistoryTitle = findViewById<TextView>(R.id.searchHistoryTitle)
        buttonclearSearchHistory = findViewById<Button>(R.id.clearSearchHistoryButton)
        searchHist = SearchHistory(sharedPrefs)


        bBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchingLine.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchingLine.windowToken, 0)
            searchingLine.clearFocus()
            showViewByFlag(
                isVisibleResultsList = false,
                isVisibleNothingFound = false,
                isVisibleNetworkProblem = false
            )
        }

        val searchLineTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                setHistoryVisibility(searchingLine.hasFocus() && s?.isEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
                textInSearchLine = s.toString()
            }
        }
        searchingLine.addTextChangedListener(searchLineTextWatcher)
        searchingLine.setOnFocusChangeListener { _, hasFocus ->
            // проверяем пуста ли история
            val isHistoryEmpty = searchHist.getHistoryTracks().isEmpty()
            // Включаем видимость истории только в случае если:
            // -фокус на строке поиска,
            // -пустая строка поиска,
            // -не пустая история
            val isVisibleHistory = hasFocus
                    && searchingLine.text.isEmpty()
                    && !isHistoryEmpty
            setHistoryVisibility(isVisibleHistory)
            if (isVisibleHistory) updateRecyclerViewByHistory()
        }
        buttonclearSearchHistory.setOnClickListener {
            searchHist.clearHistory()
            updateRecyclerViewByHistory()
            // после очистки истории скрываем панель истории
            setHistoryVisibility(false)
        }

        rvTracksContainer.layoutManager = LinearLayoutManager(this)
        rvTracksContainer.adapter = adapterTracks

        searchingLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearch()
                true
            }
            false
        }

        searchUpdateButton.setOnClickListener {
            startSearch()
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun startSearch() {
        Log.d(
            "SearchLogTag",
            "startSearch searchingLine.text.toString()==${searchingLine.text.toString()}"
        );
        itunesService.getTracks(searchingLine.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    showViewByFlag(
                        isVisibleResultsList = false,
                        isVisibleNothingFound = false,
                        isVisibleNetworkProblem = false
                    )

                    Log.d("SearchLogTag", "onResponse code==${response.code().toString()}");
                    if (response.code() == 200) {
                        listTracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            listTracks.addAll(response.body()?.results!!)
                            adapterTracks.notifyDataSetChanged()
                        }
                        if (listTracks.isEmpty()) {
                            showViewByFlag(
                                isVisibleResultsList = false,
                                isVisibleNothingFound = true,
                                isVisibleNetworkProblem = false
                            )
                        } else {
                            showViewByFlag(
                                isVisibleResultsList = true,
                                isVisibleNothingFound = false,
                                isVisibleNetworkProblem = false
                            )
                        }
                    } else {
                        showViewByFlag(
                            isVisibleResultsList = false,
                            isVisibleNothingFound = false,
                            isVisibleNetworkProblem = true
                        )
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showViewByFlag(
                        isVisibleResultsList = false,
                        isVisibleNothingFound = false,
                        isVisibleNetworkProblem = true
                    )
                    Log.d("SearchLogTag", "onFailure: $t");
                }
            })
    }

    // обработка нажатия на трэк в списке трэков
    override fun onTrackClick(track: Track) {
        searchHist.addNewTrack(track)
        if (buttonclearSearchHistory.isVisible)
            updateRecyclerViewByHistory()
        // передаём информацию о треке в плэйер
        val playerIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
        val jsonStr = Gson().toJson(track)
        playerIntent.putExtra(Track::class.simpleName, jsonStr)
        startActivity(playerIntent)
    }

    fun updateRecyclerViewByHistory() {
        listTracks.clear()
        listTracks.addAll(searchHist.getHistoryTracks())
        adapterTracks.notifyDataSetChanged()
    }

}