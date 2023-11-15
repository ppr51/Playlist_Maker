package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
        const val IS_DARK_THEME_ENABLED_KEY = "key_is_dark_theme_enabled"
    }

    private var darkTheme: Boolean = false
    lateinit var sharedPrefs: SharedPreferences

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)

        val isSystemDarkOn = isDarkThemeEnabledOnSystem(this)
        val isDarkOnInPrefrences =
            sharedPrefs.getString(IS_DARK_THEME_ENABLED_KEY, isSystemDarkOn.toString()).toBoolean()
//        Log.d("ThemeLogTag", "get  var isDarkOnInPrefrences==${isDarkOnInPrefrences.toString()}");

        switchTheme(isDarkOnInPrefrences)
    }

    fun isDarkThemeInSettings(): Boolean = darkTheme

    private fun isDarkThemeEnabledOnSystem(context: Context): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit()
            .putString(IS_DARK_THEME_ENABLED_KEY, darkTheme.toString())
            .apply()
//        Log.d("ThemeLogTag", "set  var darkTheme==${darkTheme.toString()}");
    }

}