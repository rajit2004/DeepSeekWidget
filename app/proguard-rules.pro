# ============================================================
#  DeepSeekWidget — ProGuard / R8 Rules
# ============================================================

# ── AndroidX Core ────────────────────────────────────────────
# FileProvider is instantiated by the system via AndroidManifest meta-data.
-keep class androidx.core.content.FileProvider { *; }

# ── AppWidgetProvider ────────────────────────────────────────
# Widget receivers are resolved by name from the manifest.
-keep class * extends android.appwidget.AppWidgetProvider { *; }

# ── VoiceInputActivity ───────────────────────────────────────
# Referenced by explicit intent — safe, but keep as a safeguard.
-keep class com.yourdomain.deepseekwidget.VoiceInputActivity { *; }

# ── Kotlin metadata (optional, keeps stack traces readable) ──
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
