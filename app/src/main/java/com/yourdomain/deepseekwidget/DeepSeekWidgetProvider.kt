package com.yourdomain.deepseekwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

class DeepSeekWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.deepseek_widget)

            // Whole widget → open DeepSeek app or web chat
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("deepseek://chat")
                // Fallback: open DeepSeek web chat
                val webFallback = Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.deepseek.com"))
                val chooser = Intent.createChooser(webFallback, "Open DeepSeek")
                putExtra(Intent.EXTRA_INTENT, chooser)
            }
            val openPendingIntent = PendingIntent.getActivity(
                context,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, openPendingIntent)

            // Microphone button → voice command (or fallback to open app)
            val voiceIntent = Intent(Intent.ACTION_VOICE_COMMAND)
            val voiceFallback = Intent(Intent.ACTION_VIEW, Uri.parse("deepseek://chat"))
            val voiceChooser = Intent.createChooser(voiceFallback, "Voice input")
            val voicePendingIntent = PendingIntent.getActivity(
                context,
                1,
                voiceChooser,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.mic_button, voicePendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}