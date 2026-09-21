package com.yourdomain.deepseekwidget

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.yourdomain.deepseekwidget.Constants.DEEPSEEK_PACKAGE

/**
 * Transparent Activity that shows a text input dialog.
 * Launched from the widget's text field area.
 * On submit, sends the text to DeepSeek via ACTION_SEND.
 */
class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val promptPrefix = intent?.getStringExtra(LauncherActivity.EXTRA_PROMPT_PREFIX)

        val inputView = layoutInflater.inflate(R.layout.dialog_input, null)
        val editText = inputView.findViewById<EditText>(R.id.input_field)

        if (promptPrefix != null) {
            editText.setText(promptPrefix)
            editText.setSelection(promptPrefix.length)
        }

        AlertDialog.Builder(this, R.style.Theme_DeepSeekWidget_Dialog)
            .setTitle(R.string.input_dialog_title)
            .setView(inputView)
            .setPositiveButton(R.string.input_send) { _, _ ->
                val text = editText.text.toString().trim()
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

    private fun shareTextToDeepSeek(text: String) {
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
            Toast.makeText(this, R.string.deepseek_open_error, Toast.LENGTH_SHORT).show()
        } finally {
            finish()
        }
    }

    companion object {
        private const val TAG = "InputActivity"
    }
}
