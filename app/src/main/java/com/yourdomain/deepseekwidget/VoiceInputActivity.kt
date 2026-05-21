package com.yourdomain.deepseekwidget

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.core.content.ContextCompat
import java.util.Locale

class VoiceInputActivity : Activity() {

    private companion object {
        const val REQUEST_SPEECH = 100
        const val DEEPSEEK_PACKAGE = "com.deepseek.chat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start voice recognition immediately
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        if (speechIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(speechIntent, REQUEST_SPEECH)
        } else {
            // No voice recognizer – fallback to opening DeepSeek
            openDeepSeek(null)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SPEECH) {
            if (resultCode == RESULT_OK && data != null) {
                val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = matches?.firstOrNull() ?: ""
                openDeepSeek(spokenText)
            } else {
                openDeepSeek(null)
            }
        }
    }

    private fun openDeepSeek(query: String?) {
        val uri = if (query.isNullOrBlank()) {
            Uri.parse("deepseek://chat")
        } else {
            Uri.parse("deepseek://chat?query=${Uri.encode(query)}")
        }
        val deepseekIntent = if (isPackageInstalled(DEEPSEEK_PACKAGE)) {
            Intent(Intent.ACTION_VIEW, uri)
        } else {
            // fallback to web chat with optional query (currently DeepSeek web doesn't support query param, so just open homepage)
            Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.deepseek.com"))
        }
        startActivity(deepseekIntent)
        finish()
    }

    private fun isPackageInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}