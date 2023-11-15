package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bBack = findViewById<Button>(R.id.back_from_settings_to_main_button)
        bBack.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).isDarkThemeInSettings()
//        Log.d("ThemeLogTag", "themeSwitcher.isChecked==${themeSwitcher.isChecked}");
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val bShareLinkOnApp = findViewById<Button>(R.id.shareLinkOnApp)
        bShareLinkOnApp.setOnClickListener {
            val shareLinkOnAppIntent = Intent(Intent.ACTION_SEND)
            shareLinkOnAppIntent.type = "text/plain"
            val linkOnApp = getString(R.string.link_on_app)
            shareLinkOnAppIntent.putExtra(Intent.EXTRA_TEXT, linkOnApp)
            startActivity(shareLinkOnAppIntent)
        }

        val bConnectToSupport = findViewById<Button>(R.id.connectToSupport)
        bConnectToSupport.setOnClickListener {
            val connectToSupportIntent = Intent(Intent.ACTION_SENDTO)
            connectToSupportIntent.type = "text/plain"
            val supportEmail = getString(R.string.support_email)
            val supportEmailTheme = getString(R.string.support_email_theme)
            val supportEmailBody = getString(R.string.support_email_body)
            connectToSupportIntent.data = Uri.parse(
                "mailto:$supportEmail?subject="
                        + Uri.encode(supportEmailTheme) + "&body=" + Uri.encode(supportEmailBody)
            )
            startActivity(connectToSupportIntent)
        }

        val bUserAgreement = findViewById<Button>(R.id.userAgreement)
        bUserAgreement.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.type = "text/plain"
            val userAgreementLink = getString(R.string.user_agreement_link)
            userAgreementIntent.data = Uri.parse(userAgreementLink)
            startActivity(userAgreementIntent)
        }
    }

}