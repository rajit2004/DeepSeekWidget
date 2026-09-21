package com.yourdomain.deepseekwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

/**
 * Home-screen widget provider for DeepSeekWidget.
 *
 * Responsibilities:
 *  - Inflate and bind [RemoteViews] for every placed widget instance.
 *  - Attach [PendingIntent]s for the three tap targets: root, mic, camera.
 *  - Rebuild widgets on system configuration changes (e.g., dark mode toggle).
 */
class DeepSeekWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_CONFIGURATION_CHANGED) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(
                ComponentName(context, DeepSeekWidgetProvider::class.java)
            )
            onUpdate(context, appWidgetManager, ids)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted: ${appWidgetIds.size} instance(s) removed")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled: last widget instance removed")
    }

    companion object {
        private const val TAG = "DeepSeekWidgetProvider"

        /**
         * Builds or refreshes the [RemoteViews] for a single widget instance.
         */
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.deepseek_widget)

            // ── Text input area → InputActivity (text dialog) ─────────────
            views.setOnClickPendingIntent(
                R.id.text_input_area,
                buildInputActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10)
            )

            // ── Mic button → voice trampoline ───────────────────────────
            views.setOnClickPendingIntent(
                R.id.mic_button,
                buildActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10 + 1) {
                    putExtra(Constants.EXTRA_LAUNCH_VOICE, true)
                    data = Uri.parse("widget://mic/$appWidgetId")
                }
            )

            // ── Camera button → camera trampoline ───────────────────────
            views.setOnClickPendingIntent(
                R.id.camera_button,
                buildActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10 + 2) {
                    putExtra(Constants.EXTRA_LAUNCH_CAMERA, true)
                    data = Uri.parse("widget://camera/$appWidgetId")
                }
            )

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun buildActivityIntent(
            context: Context,
            appWidgetId: Int,
            requestCode: Int,
            configure: Intent.() -> Unit = {}
        ): PendingIntent {
            val intent = Intent(context, VoiceInputActivity::class.java).apply(configure)
            return PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        private fun buildInputActivityIntent(
            context: Context,
            appWidgetId: Int,
            requestCode: Int
        ): PendingIntent {
            val intent = Intent(context, InputActivity::class.java)
            return PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
