# 🐳 DeepSeekWidget — One-Tap AI Access

> An open-source Android home screen widget that gives you instant access to DeepSeek — chat, voice input, and camera capture, without unlocking the app.

[![GitHub Release](https://img.shields.io/github/v/release/rajit2004/DeepSeekWidget?style=for-the-badge&logo=android&color=00D4AA)](https://github.com/rajit2004/DeepSeekWidget/releases)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=for-the-badge&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)
[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-purple?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)

---

## 📱 Download & Install

[![Download APK](https://img.shields.io/badge/Download-Latest%20APK-blue?style=for-the-badge&logo=github)](https://github.com/rajit2004/DeepSeekWidget/releases/latest)

> ✅ **v1.2 is live!** A debug-signed `app-debug.apk` (~1.4 MB) is available on the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases).

**Installation steps:**

1. Download `app-debug.apk` from the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases/latest)
2. On your Android device, go to **Settings → Security** and enable **"Install from unknown sources"**
3. Open the downloaded APK and install it
4. Long-press your home screen → **Widgets** → **DeepSeek Widget**

> ⚠️ This is a debug-signed APK intended for sideloading and local testing. Full source code is available in this repository for building a production-signed release yourself.

---

## 🚀 Features

| Feature | Description |
|---|---|
| 🏠 **One-tap chat** | Opens the DeepSeek app or falls back to DeepSeek web if the app isn't installed |
| 🎤 **Voice input** | Tap the mic, speak your query — DeepSeek opens with your transcribed text |
| 📷 **Camera capture** | Snap a photo directly from the widget and send it to DeepSeek |
| 🎨 **Material You design** | Adapts to your system light/dark theme with DeepSeek's teal accent |
| 📦 **Lightweight** | Zero background services, minimal permissions, ~1.4 MB APK |
| 🕊️ **Open source** | MIT licensed — free to fork, modify, and build upon |

---

## 🧠 How It Works

```
User taps widget on home screen
        ↓
PendingIntent fires
        ↓
DeepSeek app launches  ──── (fallback) ────→  DeepSeek web chat opens

Mic button → runtime RECORD_AUDIO permission → voice recognition → text sent to DeepSeek
Camera button → runtime CAMERA permission → photo captured via FileProvider → sent to DeepSeek
```

**Widget Logic:**
- The widget is a standard `AppWidgetProvider` using `RemoteViews`
- `PendingIntent`s are attached to the chat, mic, and camera buttons
- A transparent trampoline `Activity` handles voice recognition and camera capture, then redirects to DeepSeek
- Runtime permissions (microphone, camera) are requested correctly on Android 6+
- Shared constants (e.g. `DEEPSEEK_PACKAGE`) live in a single `Constants.kt` to avoid duplication

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 1.9+ |
| UI | XML (RemoteViews) |
| Build System | Gradle with Kotlin DSL (KTS) |
| AGP | 8.7.3 |
| Minimum SDK | Android 8.0 (API 26) |
| Target / Compile SDK | Android 15 (API 35) |
| Shrinking | R8 enabled (APK ~1.4 MB) |

---

## 📂 Project Structure

```
DeepSeekWidget/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── java/com/yourdomain/deepseekwidget/
│       │   ├── Constants.kt                  ← Shared constants (package names, etc.)
│       │   ├── DeepSeekWidgetProvider.kt      ← AppWidgetProvider: widget logic & PendingIntents
│       │   └── VoiceInputActivity.kt          ← Trampoline: voice recognition & camera capture
│       ├── res/
│       │   ├── drawable/
│       │   │   ├── ic_deepseek_whale.xml
│       │   │   ├── ic_mic.xml
│       │   │   └── widget_background.xml
│       │   ├── layout/
│       │   │   └── deepseek_widget.xml
│       │   ├── values/
│       │   │   ├── colors.xml
│       │   │   ├── strings.xml
│       │   │   └── themes.xml
│       │   └── xml/
│       │       └── deepseek_widget_info.xml
│       └── AndroidManifest.xml
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts                           ← Project-level build file
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── .gitignore
├── LICENSE
└── README.md
```

---

## ⚡ Build Locally

**Prerequisites:** Android Studio (Hedgehog or later) or a JDK 17+ environment with the Android SDK.

```bash
# 1. Clone the repository
git clone https://github.com/rajit2004/DeepSeekWidget.git
cd DeepSeekWidget

# 2. Open in Android Studio and let Gradle sync automatically
#    — OR — build from the command line:

# Debug APK
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Release APK (requires a keystore)
./gradlew assembleRelease
```

**To generate a production-signed release APK in Android Studio:**

1. **Build → Generate Signed Bundle / APK → APK**
2. Create a new keystore (or use an existing one)
3. Fill in the alias, passwords, and certificate details
4. Select the `release` build variant and finish

---

## 📋 Changelog

### v1.1 — Bug Fix & Stability Release *(Latest)*
- ✅ Runtime `CAMERA` and `RECORD_AUDIO` permissions now requested correctly on Android 6+
- ✅ Camera screen no longer freezes if image file creation fails
- ✅ Photo path now persists across process death during camera capture
- ✅ Double padding on widget layout removed — icons no longer cramped
- ✅ Migrated from deprecated `startActivityForResult` to `ActivityResultContracts`
- ✅ URI permission now granted only to the resolved camera app (not all apps)
- ✅ Temp image file deleted after sharing to avoid storage bloat
- ✅ Specific exception handling replacing generic `catch (Exception)`
- ✅ User-facing toasts added for all failure states
- ✅ `DEEPSEEK_PACKAGE` constant deduplicated into `Constants.kt`
- ✅ Dead code (`isPackageInstalled`) removed
- ✅ AGP bumped to 8.7.3, compileSdk/targetSdk bumped to 35
- ✅ R8 shrinking enabled — APK reduced from ~4.5 MB to ~1.4 MB

### v1.0 — First Release *(Pre-release)*
- ✅ One-tap home screen widget to open DeepSeek (app or web fallback)
- ✅ Voice input button with trampoline activity for voice recognition
- ✅ Material You design with DeepSeek teal accent
- ✅ Supports Android 8.0 (API 26) and above

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! To get started:

1. **Fork** the repository
2. **Create** a feature branch — `git checkout -b feature/your-idea`
3. **Commit** your changes — `git commit -m "Add: your feature"`
4. **Push** to your branch — `git push origin feature/your-idea`
5. **Open a Pull Request** against `main`

Please open an issue first for major changes so we can discuss the approach before you build it.

---

## 💖 Support This Project

Everything here is free and open source. If DeepSeekWidget saved you time or inspired your own project:

[![Sponsor](https://img.shields.io/badge/Sponsor-%E2%9D%A4-pink?style=for-the-badge&logo=githubsponsors)](https://github.com/sponsors/rajit2004)

---

## 👨‍💻 Author

**Ranesh Rajit** — B.Tech Computer Science Student, India

[![GitHub](https://img.shields.io/badge/GitHub-rajit2004-black?style=flat&logo=github)](https://github.com/rajit2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ranesh--kun-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ranesh-kun)

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) — see the `LICENSE` file for full details.

Free to use, modify, and distribute with attribution.

> This is an independent open-source project and is **not** officially affiliated with or endorsed by DeepSeek.
