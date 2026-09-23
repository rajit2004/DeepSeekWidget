# DeepSeek Widget

> Quick access to DeepSeek AI from your Android home screen. Type, speak, or snap a photo and send it straight to DeepSeek.

[![GitHub Release](https://img.shields.io/github/v/release/rajit2004/DeepSeekWidget?style=for-the-badge&logo=android&color=00D4AA)](https://github.com/rajit2004/DeepSeekWidget/releases)
[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=for-the-badge&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)

---

## Download

[![Download APK](https://img.shields.io/badge/Download-Latest%20APK-blue?style=for-the-badge&logo=github)](https://github.com/rajit2004/DeepSeekWidget/releases/latest)

1. Grab `app-release.apk` from [Releases](https://github.com/rajit2004/DeepSeekWidget/releases/latest)
2. Allow **Install unknown apps** for your browser or file manager
3. Open the APK and install
4. Long-press home screen, go to **Widgets**, find **DeepSeek Widget**

---

## Features

| Feature | What it does |
|---|---|
| **Instant Chat** | One tap opens DeepSeek directly to chat |
| **Voice-to-Chat** | Tap mic, speak, and the transcribed text goes to DeepSeek |
| **Camera-to-Chat** | Take a photo and send it to DeepSeek for analysis |
| **Text Input** | Tap the text field, type a question, send it off |
| **Quick Prompts** | Pre-built shortcuts: Summarize, Translate, Explain Code, Write Email |
| **Prompt History** | Last 5 prompts saved, tap to resend, long-press to copy, one-tap clear |
| **Missing-App Fallback** | If DeepSeek is not installed, text is copied and web chat opens |
| **Compact Layout** | Narrow sizes hide history and keep the three core actions |
| **Light/Dark Theme** | Follows your system theme (Material 3) |
| **Resizable Widget** | Works at 2x2, 4x1, 2x4, and other sizes |
| **Lightweight** | Around 1.5 MB APK, no background services, no battery drain |
| **Privacy First** | No data collected, no network calls, no backup. Just a router to the official DeepSeek app |

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

- `FileProvider` shares camera images securely (empty/oversize captures are rejected)
- `PendingIntent` uses `FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE` with unique data URIs per widget and action
- Widget refreshes automatically after every send via `ShareHelper.refreshWidgets`
- Notifications are skipped when `POST_NOTIFICATIONS` is not granted (Android 13+)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| Architecture | Trampoline Activity + Intent Routing |
| Security | FileProvider (Scoped Storage) |
| UI | XML RemoteViews + Material 3 |
| Minimum SDK | Android 8.0 (API 26) |
| Target SDK | Android 15 (API 35) |
| Build | Gradle KTS + R8 shrinking |

---

## Project Structure

```
DeepSeekWidget/
├── app/src/main/
│   ├── java/com/rajit2004/deepseekwidget/
│   │   ├── Constants.kt              -- Package IDs, intent extras
│   │   ├── DeepSeekWidgetProvider.kt -- Widget lifecycle, RemoteViews, PendingIntents
│   │   ├── VoiceInputActivity.kt     -- Camera + voice capture logic
│   │   ├── InputActivity.kt          -- Text input dialog
│   │   ├── HistoryActivity.kt        -- Recent prompts list
│   │   ├── ShareHelper.kt            -- Clipboard, web fallback, widget refresh
│   │   ├── NotificationHelper.kt     -- Processing notifications
│   │   └── PromptStore.kt            -- Recent prompts storage
│   ├── res/
│   │   ├── drawable/                  -- Icons, backgrounds, adaptive icon layers
│   │   ├── layout/                    -- Widget, compact widget, dialog layouts
│   │   ├── mipmap-anydpi-v26/         -- Adaptive launcher icon
│   │   ├── values/                    -- colors, strings, themes
│   │   ├── values-night/              -- Dark theme colors
│   │   └── xml/                       -- Widget info, shortcuts, FileProvider paths
│   └── AndroidManifest.xml
├── app/src/test/                     -- Robolectric unit tests (PromptStore)
├── fastlane/metadata/android/en-US/  -- Store listing + changelogs
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

Run tests with `./gradlew testDebugUnitTest`.

---

## Troubleshooting

| Problem | Fix |
|---|---|
| DeepSeek app not installed | The widget copies your text and opens web chat instead |
| Widget does not update | Remove and re-add the widget, then send one prompt to refresh |
| Camera shows an error | Check camera permission, free up storage, try again |
| Voice input missing | Install a speech recognition app (e.g. Google app) and grant mic permission |
| No notification shown | Grant notification permission on Android 13+, or ignore, it is only a progress hint |

---

## Contributing

[![Contributing Guide](https://img.shields.io/badge/Read-Contributing%20Guide-00D4AA?style=flat&logo=github)](CONTRIBUTING.md)

Bug fixes, new features, OEM testing, and translations are welcome.
See [CONTRIBUTING.md](CONTRIBUTING.md) for setup instructions.

---

## Author

**Ranesh Rajit** - B.Tech CS Student, India

[![GitHub](https://img.shields.io/badge/GitHub-rajit2004-black?style=flat&logo=github)](https://github.com/rajit2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ranesh--kun-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ranesh-kun)

---

## License

MIT - free to use, modify, and distribute with attribution.

> Independent open-source project. Not affiliated with or endorsed by DeepSeek.
