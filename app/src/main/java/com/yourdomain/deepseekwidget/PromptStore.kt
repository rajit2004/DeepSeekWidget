package com.yourdomain.deepseekwidget

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

/**
 * Stores recent prompts sent to DeepSeek.
 * Keeps the last 5 entries, newest first.
 */
class PromptStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun savePrompt(text: String) {
        val prompts = getPrompts().toMutableList()
        prompts.add(0, text.trim())
        val trimmed = prompts.take(MAX_PROMPTS)
        val jsonArray = JSONArray(trimmed)
        prefs.edit().putString(KEY_PROMPTS, jsonArray.toString()).apply()
    }

    fun getPrompts(): List<String> {
        val json = prefs.getString(KEY_PROMPTS, null) ?: return emptyList()
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).map { jsonArray.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getLatestPrompt(): String? = getPrompts().firstOrNull()

    fun clear() {
        prefs.edit().remove(KEY_PROMPTS).apply()
    }

    companion object {
        private const val PREFS_NAME = "deepseek_prompts"
        private const val KEY_PROMPTS = "prompts"
        private const val MAX_PROMPTS = 5
    }
}
