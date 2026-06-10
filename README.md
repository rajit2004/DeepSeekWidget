# 🐳 DeepSeekWidget — One-Tap AI Access

> An open-source Android home screen widget that provides instant, high-performance access to DeepSeek's core features — chat, voice input, and camera capture.

[![GitHub Release](https://img.shields.io/github/v/release/rajit2004/DeepSeekWidget?style=for-the-badge&logo=android&color=00D4AA)](https://github.com/rajit2004/DeepSeekWidget/releases)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=for-the-badge&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)
[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-purple?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)

---

## 📱 Download & Install

[![Download APK](https://img.shields.io/badge/Download-Latest%20APK-blue?style=for-the-badge&logo=github)](https://github.com/rajit2004/DeepSeekWidget/releases/latest)

> ✅ **v1.2 is live!** Fixed camera and voice routing. Download the `app-debug.apk` (~1.0 MB) from the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases).

**Installation steps:**

1. Download `app-debug.apk` from the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases/latest)
2. On your Android device, go to **Settings → Apps → Special app access → Install unknown apps** (or similar) and enable for your browser/file manager.
3. Open the downloaded APK and install it.
4. Long-press your home screen → **Widgets** → **DeepSeek Widget**.

> ⚠️ This is a debug-signed APK. For maximum security, you can build a production-signed release yourself using the source code provided.

---

## 🚀 Features

| Feature | Description |
|---|---|
| 🏠 **Instant Chat** | One-tap to open the DeepSeek app's main chat interface. |
| 🎤 **Voice-to-Chat** | Tap the mic, speak your query, and it's automatically shared to DeepSeek as a new message. |
| 📷 **Visual Search** | Snap a photo directly from your home screen and send it as an attachment to DeepSeek for analysis. |
| 🎨 **Native Feel** | Minimalist design with DeepSeek's signature teal accent; adapts to light/dark themes. |
| 📦 **Ultra-Lightweight** | Optimized with R8; tiny APK footprint (~1.0 MB) with zero background battery drain. |
| 🕊️ **Privacy First** | No data is collected by this widget. It acts purely as a high-speed router to the official DeepSeek app. |

---

## 🧠 How It Works

DeepSeekWidget uses a **"Capture and Share"** architecture to overcome the limitations of standard deep links:

1. **User Interaction**: Tapping a button fires a `PendingIntent` to a transparent `VoiceInputActivity`.
2. **Native Capture**:
   - **Mic**: Launches the system speech recognizer.
   - **Camera**: Launches the system camera and saves the image to a secure local cache via `FileProvider`.
3. **Smart Routing**: The activity packages the transcribed text or the captured image into an `ACTION_SEND` intent targeted specifically at `com.deepseek.chat`.
4. **App Hand-off**: DeepSeek receives the content and immediately presents it to the user in the chat composer.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 1.9+ |
| Architecture | Trampoline Activity / Intent Routing |
| Security | FileProvider (Scoped Storage) |
| UI | XML RemoteViews / Material Components |
| Minimum SDK | Android 8.0 (API 26) |
| Target SDK | Android 15 (API 35) |
| Optimization | R8 Shrinking & Resource Stripping |

---

## 📂 Project Structure

```
DeepSeekWidget/
├── app/
│   ├── src/main/
│   │   ├── java/com/yourdomain/deepseekwidget/
│   │   │   ├── Constants.kt             ← Package IDs and intent extras
│   │   │   ├── DeepSeekWidgetProvider.kt ← Widget lifecycle & RemoteViews
│   │   │   └── VoiceInputActivity.kt     ← Logic for Camera/Voice capture
│   │   └── res/
│   │       ├── layout/
│   │       │   └── deepseek_widget.xml   ← Widget UI declaration
│   │       └── xml/
│   │           └── file_paths.xml        ← Secure FileProvider configuration
├── build.gradle.kts                      ← Project configuration
└── README.md                             ← This documentation
```

---

## ⚡ Build Locally

```bash
# 1. Clone the repo
git clone https://github.com/rajit2004/DeepSeekWidget.git
cd DeepSeekWidget

# 2. Build Debug APK
./gradlew assembleDebug
```

---

## 📋 Changelog

### v1.2 — The "Action" Update *(Current)*
- ✅ **Fixed Camera/Voice Routing**: Implemented "Capture and Share" flow to ensure Mic/Camera buttons actually send content to DeepSeek.
- ✅ **Stability Fix**: Resolved `FileProvider` crash (`IllegalArgumentException`) when capturing photos on certain devices.
- ✅ **Permission Management**: Added runtime permission requests for `CAMERA` and `RECORD_AUDIO`.
- ✅ **Optimization**: Further reduced APK size through R8 optimizations (~1.0 MB).

### v1.1 — Refactoring
- ✅ Migrated to `Constants.kt` for better maintainability.
- ✅ Bumped Target SDK to 35 (Android 15).

### v1.0 — Initial Release
- ✅ Basic widget layout with DeepSeek teal styling.
- ✅ Web fallback support for devices without the native app.

---

## 🤝 Contributing

We love contributions!
1. Fork it.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 👨‍💻 Author

**Ranesh Rajit** — B.Tech Computer Science Student, India

[![GitHub](https://img.shields.io/badge/GitHub-rajit2004-black?style=flat&logo=github)](https://github.com/rajit2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ranesh--kun-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ranesh-kun)

---

## 📄 License

MIT License — free for everyone.

> *Disclaimer: This is an independent open-source project and is not officially affiliated with or endorsed by DeepSeek.*
