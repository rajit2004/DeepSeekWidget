package com.yourdomain.deepseekwidget

/**
 * Centralized constants for the DeepSeekWidget project.
 * All cross-file constants live here to avoid duplication and refactor risk.
 */
internal object Constants {

    /** Package name of the official DeepSeek Android app. */
    const val DEEPSEEK_PACKAGE = "com.deepseek.chat"

    /** Web fallback URL when the DeepSeek app is not installed. */
    const val DEEPSEEK_WEB_URL = "https://chat.deepseek.com"

    /** Bundle key used to persist the camera photo path across process death. */
    const val KEY_PHOTO_PATH = "photo_path"

    /** Extra key sent from [DeepSeekWidgetProvider] to [VoiceInputActivity]. */
    const val EXTRA_SKIP_VOICE = "SKIP_VOICE"

    /** Extra key sent from [DeepSeekWidgetProvider] to [VoiceInputActivity]. */
    const val EXTRA_LAUNCH_CAMERA = "LAUNCH_CAMERA"
}