package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText

class SearchActivity : AppCompatActivity() {

    private var textInSearchLine: String = ""
    private lateinit var searchingLine:EditText
    private lateinit var bBack:Button
    private lateinit var clearButton:Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchingLine = findViewById<EditText>(R.id.searchingLine)
        bBack = findViewById<Button>(R.id.back_from_search_to_main_button)
        clearButton = findViewById<Button>(R.id.clearButton)

        bBack.setOnClickListener{
            finish()
        }

        clearButton.setOnClickListener {
            searchingLine.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchingLine.windowToken, 0)
            searchingLine.clearFocus()
        }

        val searchLineTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
                textInSearchLine = s.toString()
            }
        }
        searchingLine.addTextChangedListener(searchLineTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}