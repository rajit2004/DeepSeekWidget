# ============================================================
#  DeepSeekWidget — ProGuard / R8 Rules
# ============================================================

# ── AndroidX Core ────────────────────────────────────────────
# FileProvider is instantiated by the system via AndroidManifest meta-data.
-keep class androidx.core.content.FileProvider { *; }

# ── AppWidgetProvider ────────────────────────────────────────
# Widget receivers are resolved by name from the manifest.
-keep class * extends android.appwidget.AppWidgetProvider { *; }

# ── App classes ─────────────────────────────────────────────
# Keep all classes referenced by explicit intents from the manifest.
-keep class com.yourdomain.deepseekwidget.** { *; }

# ── Kotlin metadata (optional, keeps stack traces readable) ──
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
