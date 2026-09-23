package com.rajit2004.deepseekwidget

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

/**
 * Stores recent prompts sent to DeepSeek.
 * Keeps the last 5 entries, newest first.
 */
class PromptStore(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun savePrompt(text: String) {
        val cleaned = text.trim()
        if (cleaned.isEmpty()) return
        val prompts = getPrompts().toMutableList()
        if (prompts.firstOrNull() == cleaned) return
        prompts.add(0, cleaned)
        val trimmed = prompts.take(MAX_PROMPTS)
        val jsonArray = JSONArray(trimmed)
        prefs.edit().putString(KEY_PROMPTS, jsonArray.toString()).apply()
        ShareHelper.refreshWidgets(context)
    }

    fun getPrompts(): List<String> {
        val json = prefs.getString(KEY_PROMPTS, null) ?: return emptyList()
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length())
                .map { jsonArray.getString(it) }
                .filter { it.isNotBlank() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getLatestPrompt(): String? = getPrompts().firstOrNull()

    fun clear() {
        prefs.edit().remove(KEY_PROMPTS).apply()
        ShareHelper.refreshWidgets(context)
    }

    companion object {
        private const val PREFS_NAME = "deepseek_prompts"
        private const val KEY_PROMPTS = "prompts"
        const val MAX_PROMPTS = 5
    }
}
