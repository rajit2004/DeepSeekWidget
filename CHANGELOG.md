# Changelog

All notable changes to DeepSeekWidget are documented here.
Format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

---

## [Unreleased]

---

## [2.0] - 2026-09-21

### Added
- Widget resizing support (2x2, 4x1, 2x4, and other configurations)
- Quick prompt shortcuts (Summarize, Translate, Explain Code, Write Email)
- Recent prompts history, last 5 prompts saved and accessible
- Notification channel for processing feedback
- Launcher activity anchoring static shortcuts
- PromptStore for persisting recent prompts
- LIGHT/DARK theme support, follows system setting
- Runtime permission requests for CAMERA and RECORD_AUDIO
- Temp image file cleanup after sharing
- Proper TalkBack content descriptions on all interactive elements

### Fixed
- FileProvider crash on some devices due to authority mismatch
- Camera button now sends photos to DeepSeek instead of failing silently
- Voice button now sends transcribed text instead of just opening the app
- Double padding bug in widget layout
- AppCompat theme crash when using bare Activity
- PendingIntent collision risk with unique request codes per widget instance

### Changed
- Migrated from startActivityForResult to ActivityResultContracts
- R8 full mode for smaller APK (around 1 MB)
- compileSdk and targetSdk bumped to 35 (Android 15)
- Kotlin bumped to 2.0
- Gradle wrapper bumped to 8.9

---

## [1.0.0] - 2026-05-09

### Added
- Initial release
- Home screen widget with DeepSeek teal accent and whale logo
- Tap to open DeepSeek app or fall back to web
- Mic and camera buttons
- AppWidgetProvider with RemoteViews layout
- PendingIntent with FLAG_IMMUTABLE for Android 12+ compliance
- FileProvider for scoped camera image storage
- MIT License

---

[Unreleased]: https://github.com/rajit2004/DeepSeekWidget/compare/v2.0...HEAD
[2.0]: https://github.com/rajit2004/DeepSeekWidget/compare/v1.0.0...v2.0
[1.0.0]: https://github.com/rajit2004/DeepSeekWidget/releases/tag/v1.0.0
