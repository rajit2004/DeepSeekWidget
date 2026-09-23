package com.rajit2004.deepseekwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews

class DeepSeekWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_CONFIGURATION_CHANGED ||
            intent.action == ACTION_REFRESH
        ) {
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

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled: last widget instance removed")
    }

    companion object {
        private const val TAG = "DeepSeekWidgetProvider"
        const val ACTION_REFRESH = "com.rajit2004.deepseekwidget.action.REFRESH"

        internal fun refreshAll(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val compact = minWidth > 0 && minWidth < 180
            val layoutId = if (compact) {
                R.layout.deepseek_widget_compact
            } else {
                R.layout.deepseek_widget
            }
            val views = RemoteViews(context.packageName, layoutId)

            // Text input -> opens the input dialog
            views.setOnClickPendingIntent(
                R.id.text_input_area,
                buildInputActivityIntent(context, appWidgetId)
            )

            // Mic button -> voice recognition
            views.setOnClickPendingIntent(
                R.id.mic_button,
                buildActivityIntent(context, appWidgetId, ACTION_SUFFIX_VOICE) {
                    putExtra(Constants.EXTRA_LAUNCH_VOICE, true)
                    data = Uri.parse("widget://mic/$appWidgetId")
                }
            )

            // Camera button -> camera capture
            views.setOnClickPendingIntent(
                R.id.camera_button,
                buildActivityIntent(context, appWidgetId, ACTION_SUFFIX_CAMERA) {
                    putExtra(Constants.EXTRA_LAUNCH_CAMERA, true)
                    data = Uri.parse("widget://camera/$appWidgetId")
                }
            )

            // Show last prompt if available (full layout only)
            if (!compact && views.layoutId == R.layout.deepseek_widget) {
                val lastPrompt = PromptStore(context).getLatestPrompt()
                if (lastPrompt != null) {
                    views.setViewVisibility(R.id.last_prompt, View.VISIBLE)
                    views.setTextViewText(R.id.last_prompt, lastPrompt)
                } else {
                    views.setViewVisibility(R.id.last_prompt, View.GONE)
                }
            }

            // Tapping history opens the history list
            try {
                views.setOnClickPendingIntent(
                    R.id.last_prompt,
                    buildHistoryIntent(context, appWidgetId)
                )
            } catch (e: Exception) {
                // Compact layout has no last_prompt view, ignore.
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private const val ACTION_SUFFIX_INPUT = 0
        private const val ACTION_SUFFIX_VOICE = 1
        private const val ACTION_SUFFIX_CAMERA = 2
        private const val ACTION_SUFFIX_HISTORY = 3

        private fun requestCodeFor(appWidgetId: Int, suffix: Int): Int {
            return (appWidgetId * 10 + suffix) and 0x0FFFFFFF
        }

        private fun buildActivityIntent(
            context: Context,
            appWidgetId: Int,
            suffix: Int,
            configure: Intent.() -> Unit = {}
        ): PendingIntent {
            val intent = Intent(context, VoiceInputActivity::class.java).apply(configure)
            return PendingIntent.getActivity(
                context,
                requestCodeFor(appWidgetId, suffix),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        private fun buildInputActivityIntent(
            context: Context,
            appWidgetId: Int
        ): PendingIntent {
            val intent = Intent(context, InputActivity::class.java).apply {
                data = Uri.parse("widget://input/$appWidgetId")
            }
            return PendingIntent.getActivity(
                context,
                requestCodeFor(appWidgetId, ACTION_SUFFIX_INPUT),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        private fun buildHistoryIntent(
            context: Context,
            appWidgetId: Int
        ): PendingIntent {
            val intent = Intent(context, HistoryActivity::class.java).apply {
                data = Uri.parse("widget://history/$appWidgetId")
            }
            return PendingIntent.getActivity(
                context,
                requestCodeFor(appWidgetId, ACTION_SUFFIX_HISTORY),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
