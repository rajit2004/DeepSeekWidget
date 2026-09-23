package com.rajit2004.deepseekwidget

import android.appwidget.AppWidgetManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object ShareHelper {

    fun copyText(context: Context, text: String) {
        try {
            val clipboard = context.getSystemService(ClipboardManager::class.java)
            clipboard.setPrimaryClip(ClipData.newPlainText("DeepSeek prompt", text))
        } catch (e: Exception) {
            // Clipboard can throw on some OEMs, never crash the share flow.
        }
    }

    fun openWebChat(context: Context, uri: Uri = Uri.parse(Constants.DEEPSEEK_WEB_URL)) {
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.deepseek_open_error, Toast.LENGTH_SHORT).show()
        }
    }

    fun fallbackTextShare(context: Context, text: String) {
        copyText(context, text)
        openWebChat(context)
        Toast.makeText(context, R.string.deepseek_fallback_copied, Toast.LENGTH_LONG).show()
    }

    fun refreshWidgets(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(
                ComponentName(context, DeepSeekWidgetProvider::class.java)
            )
            if (ids.isNotEmpty()) {
                DeepSeekWidgetProvider.refreshAll(context, appWidgetManager, ids)
            }
        } catch (e: Exception) {
            // Widget refresh is best effort.
        }
    }
}
