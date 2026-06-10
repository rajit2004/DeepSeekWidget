# 🐳 DeepSeekWidget — One-Tap AI Access

> An open-source Android home screen widget for instant access to DeepSeek — chat, voice, and camera without unlocking your phone.

[![GitHub Release](https://img.shields.io/github/v/release/rajit2004/DeepSeekWidget?style=for-the-badge&logo=android&color=00D4AA)](https://github.com/rajit2004/DeepSeekWidget/releases)
[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=for-the-badge&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)
[![Changelog](https://img.shields.io/badge/Changelog-v1.2-00D4AA?style=for-the-badge)](CHANGELOG.md)

---

## 📱 Download & Install

[![Download APK](https://img.shields.io/badge/Download-Latest%20APK-blue?style=for-the-badge&logo=github)](https://github.com/rajit2004/DeepSeekWidget/releases/latest)

> ✅ **v1.2 is live!** Camera and voice routing fully working. Download `app-debug.apk` (~1.0 MB) from the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases).

1. Download `app-debug.apk` from the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases/latest)
2. Enable **Install unknown apps** for your browser/file manager in device settings
3. Open the APK and install
4. Long-press your home screen → **Widgets** → **DeepSeek Widget**

---

## 🚀 Features

| Feature | Description |
|---|---|
| 🏠 **Instant Chat** | One tap opens DeepSeek directly to chat — no unlock, no navigation. |
| 🎤 **Voice-to-Chat** | Tap mic → speak → transcribed text lands in DeepSeek's composer ready to send. |
| 📷 **Camera-to-Chat** | Snap a photo from the home screen and send it straight to DeepSeek for visual analysis. |
| 🎨 **Native Feel** | DeepSeek's teal accent, adapts to system light/dark theme. |
| 📦 **Ultra-Lightweight** | R8-optimized. ~1.0 MB APK, zero background services, zero battery drain. |
| 🕊️ **Privacy First** | No data collected. Widget is a pure router to the official DeepSeek app. |

---

## 🧠 How It Works

DeepSeekWidget uses a **"Capture and Share"** architecture — the only reliable way to pass content into another app from a home-screen widget:

```
Widget tap
    ↓
PendingIntent → VoiceInputActivity (transparent, no UI)
    ↓
  [Chat tap]          [Mic tap]                    [Camera tap]
  Open DeepSeek       RecognizerIntent              Camera intent + FileProvider
                      → transcript text             → JPEG saved to scoped storage
                           ↓                               ↓
                      ACTION_SEND (text/plain)       ACTION_SEND (image/jpeg)
                           ↓                               ↓
                      DeepSeek chat composer ←────────────┘
```

- `FileProvider` ensures camera images are shared securely without exposing raw file paths
- `PendingIntent` flags are set to `FLAG_IMMUTABLE` for Android 12+ compliance
- Each widget instance uses unique request codes so multiple placed widgets never conflict

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| Architecture | Trampoline Activity + Intent Routing |
| Security | FileProvider (Scoped Storage) |
| UI | XML RemoteViews + Material Components |
| Minimum SDK | Android 8.0 (API 26) |
| Target SDK | Android 15 (API 35) |
| Build | Gradle KTS + R8 shrinking |

---

## 📂 Project Structure

```
DeepSeekWidget/
├── app/src/main/
│   ├── java/com/yourdomain/deepseekwidget/
│   │   ├── Constants.kt              ← Package IDs, deep link URIs, intent extras
│   │   ├── DeepSeekWidgetProvider.kt ← Widget lifecycle, RemoteViews, PendingIntents
│   │   └── VoiceInputActivity.kt     ← Camera + voice capture logic
│   └── res/
│       ├── drawable/                 ← Widget icons and background shape
│       ├── layout/deepseek_widget.xml
│       ├── values/                   ← colors, strings, themes
│       └── xml/
│           ├── deepseek_widget_info.xml  ← Widget metadata (size, update period)
│           └── file_paths.xml            ← FileProvider path config
├── build.gradle.kts
└── README.md
```

---

## ⚡ Build Locally

```bash
git clone https://github.com/rajit2004/DeepSeekWidget.git
cd DeepSeekWidget
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

---

## 🤝 Contributing

[![Contributing Guide](https://img.shields.io/badge/Read-Contributing%20Guide-00D4AA?style=flat&logo=github)](CONTRIBUTING.md)

Bug fixes, new features, OEM testing, and translations are all welcome.  
See [CONTRIBUTING.md](CONTRIBUTING.md) for setup instructions and areas that need help.

---

## 👨‍💻 Author

**Ranesh Rajit** — B.Tech CS Student, India

[![GitHub](https://img.shields.io/badge/GitHub-rajit2004-black?style=flat&logo=github)](https://github.com/rajit2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ranesh--kun-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ranesh-kun)

---

## 📄 License

MIT — free to use, modify, and distribute with attribution.

> *Independent open-source project. Not affiliated with or endorsed by DeepSeek.*
