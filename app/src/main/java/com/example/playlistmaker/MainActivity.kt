package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bSearch = findViewById<Button>(R.id.search_button)
        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        bSearch.setOnClickListener(buttonSearchClickListener)

        val bMedia = findViewById<Button>(R.id.media_button)
        bMedia.setOnClickListener{
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        val bSettings = findViewById<Button>(R.id.settings_button)
        bSettings.setOnClickListener{
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}