package com.rajit2004.deepseekwidget

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.rajit2004.deepseekwidget.Constants.DEEPSEEK_PACKAGE
import com.rajit2004.deepseekwidget.Constants.EXTRA_PROMPT_PREFIX
import com.rajit2004.deepseekwidget.Constants.LEGACY_EXTRA_PROMPT_PREFIX

/**
 * Shows a text input dialog when the user taps the widget's text field.
 * Sends the entered text to DeepSeek via ACTION_SEND.
 */
class InputActivity : AppCompatActivity() {

    private var draftText: String? = null
    private lateinit var promptStore: PromptStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promptStore = PromptStore(this)
        draftText = savedInstanceState?.getString(KEY_DRAFT)

        val promptPrefix = intent?.getStringExtra(EXTRA_PROMPT_PREFIX)
            ?: intent?.getStringExtra(LEGACY_EXTRA_PROMPT_PREFIX)
            ?: intent?.getStringExtra(LauncherActivity.EXTRA_PROMPT_PREFIX)

        val inputView = layoutInflater.inflate(R.layout.dialog_input, null)
        val editText = inputView.findViewById<EditText>(R.id.input_field)

        val initialText = draftText ?: promptPrefix
        if (initialText != null) {
            editText.setText(initialText)
            editText.setSelection(initialText.length)
        }

        editText.setOnEditorActionListener { v, _, _ ->
            val text = v.text.toString().trim()
            if (text.isNotEmpty()) {
                draftText = null
                shareTextToDeepSeek(text)
            } else {
                finish()
            }
            true
        }

        AlertDialog.Builder(this, R.style.Theme_DeepSeekWidget_Dialog)
            .setTitle(R.string.input_dialog_title)
            .setView(inputView)
            .setPositiveButton(R.string.input_send) { _, _ ->
                val text = editText.text.toString().trim()
                if (text.isNotEmpty()) {
                    draftText = null
                    shareTextToDeepSeek(text)
                } else {
                    finish()
                }
            }
            .setNegativeButton(R.string.input_cancel) { _, _ ->
                draftText = editText.text.toString()
                finish()
            }
            .setOnCancelListener {
                finish()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_DRAFT, draftText)
    }

    private fun shareTextToDeepSeek(text: String) {
        promptStore.savePrompt(text)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to share text to DeepSeek", e)
            ShareHelper.fallbackTextShare(this, text)
        } finally {
            finish()
        }
    }

    companion object {
        private const val TAG = "InputActivity"
        private const val KEY_DRAFT = "input_draft"
    }
}
