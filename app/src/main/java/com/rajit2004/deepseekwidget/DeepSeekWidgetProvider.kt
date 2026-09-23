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

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        val minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        Log.d(TAG, "onAppWidgetOptionsChanged: $appWidgetId -> ${minWidth}x${minHeight}dp")
        updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled: last widget instance removed")
    }

    companion object {
        private const val TAG = "DeepSeekWidgetProvider"

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.deepseek_widget)

            // Text input -> opens the input dialog
            views.setOnClickPendingIntent(
                R.id.text_input_area,
                buildInputActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10)
            )

            // Mic button -> voice recognition
            views.setOnClickPendingIntent(
                R.id.mic_button,
                buildActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10 + 1) {
                    putExtra(Constants.EXTRA_LAUNCH_VOICE, true)
                    data = Uri.parse("widget://mic/$appWidgetId")
                }
            )

            // Camera button -> camera capture
            views.setOnClickPendingIntent(
                R.id.camera_button,
                buildActivityIntent(context, appWidgetId, requestCode = appWidgetId * 10 + 2) {
                    putExtra(Constants.EXTRA_LAUNCH_CAMERA, true)
                    data = Uri.parse("widget://camera/$appWidgetId")
                }
            )

            // Show last prompt if available
            val lastPrompt = PromptStore(context).getLatestPrompt()
            if (lastPrompt != null) {
                views.setViewVisibility(R.id.last_prompt, View.VISIBLE)
                views.setTextViewText(R.id.last_prompt, lastPrompt)
            } else {
                views.setViewVisibility(R.id.last_prompt, View.GONE)
            }

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
