package com.rajit2004.deepseekwidget

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.rajit2004.deepseekwidget.Constants.DEEPSEEK_PACKAGE

/**
 * Shows recent prompts with resend, copy, and clear actions.
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var promptStore: PromptStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promptStore = PromptStore(this)

        val prompts = promptStore.getPrompts()
        if (prompts.isEmpty()) {
            AlertDialog.Builder(this, R.style.Theme_DeepSeekWidget_Dialog)
                .setTitle(R.string.history_title)
                .setMessage(R.string.history_empty)
                .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
                .setOnCancelListener { finish() }
                .show()
            return
        }

        val listView = ListView(this).apply {
            adapter = ArrayAdapter(
                this@HistoryActivity,
                android.R.layout.simple_list_item_1,
                prompts
            )
        }

        AlertDialog.Builder(this, R.style.Theme_DeepSeekWidget_Dialog)
            .setTitle(R.string.history_title)
            .setView(listView)
            .setNeutralButton(R.string.history_clear) { _, _ ->
                promptStore.clear()
                Toast.makeText(this, R.string.history_cleared, Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton(R.string.input_cancel) { _, _ -> finish() }
            .setOnCancelListener { finish() }
            .show()

        listView.setOnItemClickListener { _, _, position, _ ->
            resendPrompt(prompts[position])
        }
        listView.setOnItemLongClickListener { _, _, position, _ ->
            ShareHelper.copyText(this, prompts[position])
            Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun resendPrompt(text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to resend prompt", e)
            ShareHelper.fallbackTextShare(this, text)
        } finally {
            finish()
        }
    }

    companion object {
        private const val TAG = "HistoryActivity"
    }
}
