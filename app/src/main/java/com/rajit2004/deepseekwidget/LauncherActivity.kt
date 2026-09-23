package com.rajit2004.deepseekwidget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rajit2004.deepseekwidget.Constants.EXTRA_PROMPT_PREFIX
import com.rajit2004.deepseekwidget.Constants.LEGACY_EXTRA_PROMPT_PREFIX

/**
 * Launcher activity that anchors static shortcuts.
 * Forwards to InputActivity with any prompt prefix.
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val promptPrefix = intent?.getStringExtra(EXTRA_PROMPT_PREFIX)
            ?: intent?.getStringExtra(LEGACY_EXTRA_PROMPT_PREFIX)

        val inputIntent = Intent(this, InputActivity::class.java).apply {
            if (promptPrefix != null) {
                putExtra(EXTRA_PROMPT_PREFIX, promptPrefix)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(inputIntent)
        finish()
    }

    companion object {
        const val EXTRA_PROMPT_PREFIX = Constants.EXTRA_PROMPT_PREFIX
    }
}
