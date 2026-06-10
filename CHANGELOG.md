# Changelog

All notable changes to DeepSeekWidget are documented here.
Format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

---

## [Unreleased]

---

## [1.2.0] — 2026-05-22 — The "Action" Update

### Fixed
- Camera button now works end-to-end: photo is captured and sent directly to DeepSeek's chat composer via `ACTION_SEND` with `image/jpeg`
- `FileProvider` crash (`IllegalArgumentException`) when launching the camera on certain devices — authority mismatch resolved
- Transparent activity no longer hangs open if image file creation fails — `finish()` is now called in all error paths
- Voice button now sends transcribed text to DeepSeek instead of just opening the app

### Added
- Runtime permission requests for `CAMERA` and `RECORD_AUDIO` before each respective action
- `onSaveInstanceState` saves `currentPhotoPath` so the image is not lost if the process is killed while the camera is open
- User-facing `Toast` messages for all failure states (permission denied, camera unavailable, file error, share error)
- Temp image file is deleted after sharing to prevent storage bloat

### Changed
- APK size reduced to ~1.0 MB via R8 resource shrinking (`isShrinkResources = true`)
- URI permission granted only to the resolved camera app — not broadcast to all apps

---

## [1.1.0] — 2026-05-22 — Refactoring

### Added
- `Constants.kt` — single source of truth for `DEEPSEEK_PACKAGE`, `FILE_PROVIDER_AUTHORITY`, `DEEPSEEK_WEB_URL`, and all intent extra keys
- `onDeleted` and `onDisabled` overrides in `DeepSeekWidgetProvider` for future cleanup hooks
- `android:description` in `deepseek_widget_info.xml` for Android 12+ widget picker

### Fixed
- Double padding bug: removed `<padding>` block from `widget_background.xml` — shape drawable padding was stacking on top of layout padding, compressing widget content
- `app:tint` on `ImageButton` replaced with `RemoteViews.setInt(..., "setColorFilter", ...)` — AppCompat attributes are unsupported in RemoteViews
- `VoiceInputActivity` now extends `AppCompatActivity` — bare `Activity` with an AppCompat theme caused crashes on some devices
- `@android:style/Theme.Translucent.NoTitleBar` replaced with a custom `Theme.DeepSeekWidget.Transparent` that extends AppCompat
- `FileProvider` authority now uses `${applicationId}` manifest placeholder — stays in sync with `applicationId` in `build.gradle.kts`

### Changed
- Migrated from deprecated `startActivityForResult` / `onActivityResult` to `ActivityResultContracts`
- `RECORD_AUDIO` and `CAMERA` now imported directly (`import android.Manifest.permission.CAMERA`) rather than via `android.Manifest`
- `Uri.parse()` replaced with KTX `String.toUri()` extension
- `Activity.RESULT_OK` → bare `RESULT_OK` (redundant qualifier inside `AppCompatActivity`)
- Removed dead code: `isPackageInstalled()` function that was defined but never called
- `DEEPSEEK_PACKAGE` constant deduplicated — was defined independently in both Kotlin files

### Build
- `compileSdk` and `targetSdk` bumped from 34 → 35 (Android 15)
- Kotlin bumped from 1.9.0 → 2.0.0
- Android Gradle Plugin bumped from 8.2.0 → 8.7.3 (first version with full SDK 35 support)
- Gradle wrapper bumped from 8.4 → 8.9
- `buildConfig = true` added to `buildFeatures` block
- R8 enabled for release builds: `isMinifyEnabled = true`, `isShrinkResources = true`
- Release build no longer signed with debug keystore

---

## [1.0.0] — 2026-05-09 — Initial Release

### Added
- Home screen widget with DeepSeek teal accent and whale logo
- Tap-to-open: launches DeepSeek app or falls back to `chat.deepseek.com` if not installed
- Mic button: basic voice recognition trampoline
- Camera button: initial implementation (non-functional in this release)
- `AppWidgetProvider` with `RemoteViews` layout
- `PendingIntent` with `FLAG_IMMUTABLE` for Android 12+ compliance
- Unique `requestCode` per widget instance to prevent `PendingIntent` collisions
- Programmatic teal tint on icon buttons via `setColorFilter` (avoids unsupported `app:tint` in RemoteViews)
- MIT License
- `FileProvider` configuration for scoped camera image storage

---

[Unreleased]: https://github.com/rajit2004/DeepSeekWidget/compare/v1.2.0...HEAD
[1.2.0]: https://github.com/rajit2004/DeepSeekWidget/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/rajit2004/DeepSeekWidget/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/rajit2004/DeepSeekWidget/releases/tag/v1.0.0
