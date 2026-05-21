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
            // We use the trampoline activity for the main tap too, to ensure consistent fallback logic and crash protection
            val openIntent = Intent(context, VoiceInputActivity::class.java).apply {
                // We'll tell VoiceInputActivity NOT to trigger voice recognition, just open the app
                putExtra("SKIP_VOICE", true)
                // Force a unique identity for the Intent to avoid PendingIntent caching
                data = Uri.parse("widget://main/$appWidgetId")
            }
            val openPendingIntent = PendingIntent.getActivity(
                context, appWidgetId * 10, openIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, openPendingIntent)

            // ── Mic button – launches a trampoline activity that starts voice recognition ──
            val voiceIntent = Intent(context, VoiceInputActivity::class.java).apply {
                // Force a unique identity
                data = Uri.parse("widget://mic/$appWidgetId")
            }
            val voicePendingIntent = PendingIntent.getActivity(
                context, appWidgetId * 10 + 1, voiceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.mic_button, voicePendingIntent)

            // ── Camera button ──
            val cameraIntent = Intent(context, VoiceInputActivity::class.java).apply {
                putExtra("LAUNCH_CAMERA", true)
                // Force a unique identity
                data = Uri.parse("widget://camera/$appWidgetId")
            }
            val cameraPendingIntent = PendingIntent.getActivity(
                context, appWidgetId * 10 + 2, cameraIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.camera_button, cameraPendingIntent)

            // ── Set tints programmatically for reliable rendering ──
            val teal = 0xFF00D4AA.toInt()
            views.setInt(R.id.camera_button, "setColorFilter", teal)
            views.setInt(R.id.mic_button, "setColorFilter", teal)

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