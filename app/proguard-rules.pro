# DeepSeekWidget ProGuard Rules

# AndroidX Core - FileProvider is instantiated by the system via manifest meta-data
-keep class androidx.core.content.FileProvider { *; }

# AppWidgetProvider - Widget receivers are resolved by name from the manifest
-keep class * extends android.appwidget.AppWidgetProvider { *; }

# App classes - Keep all classes referenced by explicit intents from the manifest
-keep class com.rajit2004.deepseekwidget.** { *; }

# Kotlin metadata (keeps stack traces readable)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
