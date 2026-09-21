# DeepSeek Widget — AI Chat, Voice & Camera from Your Home Screen

> One-tap access to DeepSeek AI from your Android home screen. Type, speak, or snap — instant AI chat without unlocking your phone.

[![GitHub Release](https://img.shields.io/github/v/release/rajit2004/DeepSeekWidget?style=for-the-badge&logo=android&color=00D4AA)](https://github.com/rajit2004/DeepSeekWidget/releases)
[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=for-the-badge&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)

---

## Download

[![Download APK](https://img.shields.io/badge/Download-Latest%20APK-blue?style=for-the-badge&logo=github)](https://github.com/rajit2004/DeepSeekWidget/releases/latest)

1. Download `app-release.apk` from [Releases](https://github.com/rajit2004/DeepSeekWidget/releases/latest)
2. Enable **Install unknown apps** for your browser/file manager
3. Open the APK and install
4. Long-press home screen → **Widgets** → **DeepSeek Widget**

---

## Features

| Feature | Description |
|---|---|
| **Instant Chat** | One tap opens DeepSeek directly to chat — no unlock, no navigation |
| **Voice-to-Chat** | Tap mic → speak → transcribed text sent to DeepSeek |
| **Camera-to-Chat** | Snap a photo and send it to DeepSeek for visual analysis |
| **Text Input** | Tap the text field → type a question → send directly to DeepSeek |
| **Quick Prompts** | Pre-built shortcuts: Summarize, Translate, Explain Code, Write Email |
| **Light/Dark Theme** | Adapts to your system theme automatically |
| **Resizable Widget** | Works at 2x2, 4x1, 2x4, and other sizes |
| **Ultra-Lightweight** | R8-optimized. ~1.5 MB APK, zero background services, zero battery drain |
| **Privacy First** | No data collected. Widget is a pure router to the official DeepSeek app |

---

## How It Works

```
Widget tap
    |
    +-- [Text field] --> InputActivity (AlertDialog) --> DeepSeek
    +-- [Chat area]  --> VoiceInputActivity -----------> DeepSeek
    +-- [Mic button] --> RecognizerIntent --> text ---> DeepSeek
    +-- [Camera btn] --> Camera intent --> photo -----> DeepSeek
```

- `FileProvider` ensures camera images are shared securely
- `PendingIntent` uses `FLAG_IMMUTABLE` for Android 12+ compliance
- Unique request codes per widget instance prevent PendingIntent collisions

---

## Comparison

| Feature | DeepSeek Widget | ChatGPT Widget | Gemini Widget |
|---|---|---|---|
| One-tap chat | Yes | Yes | Yes |
| Voice input | Yes | No | Yes |
| Camera input | Yes | No | Yes |
| Text input on widget | Yes | No | No |
| Quick prompt shortcuts | Yes | No | No |
| Open source | Yes (MIT) | No | No |
| Lightweight | ~1.5 MB | ~50 MB | ~80 MB |
| Battery usage | Zero | Background services | Background services |
| Requires account | No | Yes | Yes |

---

## Tech Stack

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

## Project Structure

```
DeepSeekWidget/
├── app/src/main/
│   ├── java/com/yourdomain/deepseekwidget/
│   │   ├── Constants.kt              ← Package IDs, intent extras
│   │   ├── DeepSeekWidgetProvider.kt ← Widget lifecycle, RemoteViews, PendingIntents
│   │   ├── VoiceInputActivity.kt     ← Camera + voice capture logic
│   │   ├── InputActivity.kt          ← Text input dialog
│   │   ├── NotificationHelper.kt     ← Processing notifications
│   │   └── PromptStore.kt            ← Recent prompts storage
│   └── res/
│       ├── drawable/                  ← Icons, backgrounds, adaptive icon layers
│       ├── layout/                    ← Widget layout + dialog layout
│       ├── mipmap-anydpi-v26/         ← Adaptive launcher icon
│       ├── values/                    ← colors, strings, themes
│       ├── values-night/              ← Dark theme colors
│       └── xml/                       ← Widget info, shortcuts, FileProvider paths
├── build.gradle.kts
└── README.md
```

---

## Build Locally

```bash
git clone https://github.com/rajit2004/DeepSeekWidget.git
cd DeepSeekWidget
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

---

## Contributing

[![Contributing Guide](https://img.shields.io/badge/Read-Contributing%20Guide-00D4AA?style=flat&logo=github)](CONTRIBUTING.md)

Bug fixes, new features, OEM testing, and translations are welcome.
See [CONTRIBUTING.md](CONTRIBUTING.md) for setup instructions.

---

## Author

**Ranesh Rajit** — B.Tech CS Student, India

[![GitHub](https://img.shields.io/badge/GitHub-rajit2004-black?style=flat&logo=github)](https://github.com/rajit2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ranesh--kun-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ranesh-kun)

---

## License

MIT — free to use, modify, and distribute with attribution.

> *Independent open-source project. Not affiliated with or endorsed by DeepSeek.*
