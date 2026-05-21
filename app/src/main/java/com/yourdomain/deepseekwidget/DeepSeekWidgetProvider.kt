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

        private const val DEEPSEEK_PACKAGE = "com.deepseek.chat" // adjust if necessary

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.deepseek_widget)

            // ── Main tap – opens DeepSeek app (or web fallback) ──
            val openIntent = if (isPackageInstalled(context, DEEPSEEK_PACKAGE)) {
                Intent(Intent.ACTION_VIEW, Uri.parse("deepseek://chat"))
            } else {
                Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.deepseek.com"))
            }
            val openPendingIntent = PendingIntent.getActivity(
                context, 0, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, openPendingIntent)

            // ── Mic button – launches a trampoline activity that starts voice recognition ──
            val voiceIntent = Intent(context, VoiceInputActivity::class.java)
            val voicePendingIntent = PendingIntent.getActivity(
                context, 1, voiceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.mic_button, voicePendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun isPackageInstalled(context: Context, packageName: String): Boolean {
            return try {
                context.packageManager.getPackageInfo(packageName, 0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}