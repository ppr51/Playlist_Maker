package com.example.playlistmaker

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
                Toast.makeText(this@MainActivity, "Нажали кнопку поиска!", Toast.LENGTH_SHORT).show()
            }
        }
        bSearch.setOnClickListener(buttonSearchClickListener)

        val bMedia = findViewById<Button>(R.id.media_button)
        bMedia.setOnClickListener{
            Toast.makeText(this@MainActivity,"Нажали кнопку медиа!", Toast.LENGTH_SHORT).show()
        }

        val bSettings = findViewById<Button>(R.id.settings_button)
        bSettings.setOnClickListener{
            Toast.makeText(this@MainActivity,"Нажали кнопку настроек!", Toast.LENGTH_SHORT).show()
        }
    }
}