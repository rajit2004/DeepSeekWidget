package com.rajit2004.deepseekwidget

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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

    private var editText: EditText? = null
    private lateinit var promptStore: PromptStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promptStore = PromptStore(this)

        val promptPrefix = intent?.getStringExtra(EXTRA_PROMPT_PREFIX)
            ?: intent?.getStringExtra(LEGACY_EXTRA_PROMPT_PREFIX)
            ?: intent?.getStringExtra(LauncherActivity.EXTRA_PROMPT_PREFIX)

        val inputView = layoutInflater.inflate(R.layout.dialog_input, null)
        val field = inputView.findViewById<EditText>(R.id.input_field)
        editText = field

        val initialText = savedInstanceState?.getString(KEY_DRAFT) ?: promptPrefix
        if (initialText != null) {
            field.setText(initialText)
            field.setSelection(initialText.length)
        }

        field.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val text = v.text.toString().trim()
                if (text.isNotEmpty()) {
                    shareTextToDeepSeek(text)
                } else {
                    finish()
                }
                true
            } else {
                false
            }
        }

        AlertDialog.Builder(this, R.style.Theme_DeepSeekWidget_Dialog)
            .setTitle(R.string.input_dialog_title)
            .setView(inputView)
            .setPositiveButton(R.string.input_send) { _, _ ->
                val text = editText?.text.toString().trim()
                if (text.isNotEmpty()) {
                    shareTextToDeepSeek(text)
                } else {
                    finish()
                }
            }
            .setNegativeButton(R.string.input_cancel) { _, _ ->
                finish()
            }
            .setOnCancelListener {
                finish()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_DRAFT, editText?.text?.toString())
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
