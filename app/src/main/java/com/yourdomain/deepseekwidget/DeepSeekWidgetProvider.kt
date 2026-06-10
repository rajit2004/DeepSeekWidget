package com.yourdomain.deepseekwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
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
 *  - Apply icon tints programmatically (avoids unsupported [app:tint] in RemoteViews).
 */
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

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted: ${appWidgetIds.size} instance(s) removed")
        // No persistent state to clean up in v1.0.
        // Future: clear any SharedPreferences keyed by appWidgetId here.
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled: last widget instance removed")
        // Future: cancel WorkManager tasks or alarms here.
    }

    companion object {
        private const val TAG = "DeepSeekWidgetProvider"

        /**
         * Builds or refreshes the [RemoteViews] for a single widget instance.
         * Called both from [onUpdate] and from any future configuration activity.
         */
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.deepseek_widget)

            // ── Main area tap → open DeepSeek (skip voice) ──────────────────
            views.setOnClickPendingIntent(
                R.id.widget_root,
                buildActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10) {
                    putExtra(Constants.EXTRA_SKIP_VOICE, true)
                    data = Uri.parse("widget://main/$appWidgetId")
                }
            )

            // ── Mic button → native voice trampoline ───────────────────────
            views.setOnClickPendingIntent(
                R.id.mic_button,
                buildActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10 + 1) {
                    putExtra(Constants.EXTRA_LAUNCH_VOICE, true)
                    data = Uri.parse("widget://mic/$appWidgetId")
                }
            )

            // ── Camera button → camera capture trampoline ────────────────────
            views.setOnClickPendingIntent(
                R.id.camera_button,
                buildActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10 + 2) {
                    putExtra(Constants.EXTRA_LAUNCH_CAMERA, true)
                    data = Uri.parse("widget://camera/$appWidgetId")
                }
            )

            // ── Apply teal tint programmatically ─────────────────────────────
            // app:tint is an AppCompat attribute — unsupported in RemoteViews.
            // setColorFilter via reflection is the correct way.
            val teal = 0xFF00D4AA.toInt()
            views.setInt(R.id.mic_button, "setColorFilter", teal)
            views.setInt(R.id.camera_button, "setColorFilter", teal)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        /**
         * Creates a [PendingIntent] that launches [VoiceInputActivity] with optional
         * intent customization via [configure].
         *
         * Each button uses a unique [requestCode] so Android does not collapse distinct
         * intents into the same cached [PendingIntent].
         */
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
    }
}