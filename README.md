# 🐳 DeepSeekWidget – One‑Tap AI Access

> An open‑source Android home screen widget that gives you instant access to DeepSeek — ask anything, use voice, without unlocking the app.

[![GitHub release](https://img.shields.io/github/v/release/rajit2004/DeepSeekWidget?style=for-the-badge&logo=android&color=00D4AA)](https://github.com/rajit2004/DeepSeekWidget/releases)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=for-the-badge&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)
![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-purple?style=flat&logo=kotlin)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)

---

## 📱 Download & Install

[![Get it on GitHub](https://img.shields.io/badge/Get%20APK-v1.0%20Release-blue?style=for-the-badge&logo=github)](https://github.com/rajit2004/DeepSeekWidget/releases/tag/v1.0)

> ✅ **v1.0 is live!** A signed `app-release.apk` (4.3 MB) is available on the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases).

**Installation steps:**

1. Download `app-release.apk` from the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases)
2. Install it on your phone — you may need to allow **"Install from unknown sources"**
3. Long-press your home screen → **Widgets** → **DeepSeek Widget**

> 💡 This is a signed release APK. The full source code is available in this repository.

---

## 🚀 Features

- 🏠 **One‑tap chat access** – open DeepSeek (app or web) directly from the widget
- 🎤 **Voice button** – tap the mic, speak your question, and DeepSeek opens automatically with your input (trampoline activity for voice recognition)
- 🎨 **Material You design** – adapts to light/dark system theme, uses DeepSeek's teal accent
- 📦 **Lightweight** – zero background services, minimal permissions
- 🕊️ **Open source** – built for the community, free to fork and modify

---

## 🧠 How It Works

```
User taps widget on home screen
↓
PendingIntent fires → DeepSeek app or web opens
↓
User asks question → AI responds instantly

Mic button → voice recognition activity → spoken text sent to DeepSeek
```

**Widget Logic:**
- The widget is a standard `AppWidgetProvider` using `RemoteViews`
- Click listeners attach `PendingIntent`s for chat and voice actions
- A transparent trampoline `Activity` handles voice recognition and then redirects to DeepSeek

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 1.9+ |
| UI | XML (RemoteViews) |
| Build System | Gradle (KTS) |
| Minimum SDK | Android 8.0 (API 26) |
| Target SDK | Android 14 (API 34) |

---

## 📂 Project Structure

```
DeepSeekWidget/
├── app/
│   └── src/
│       └── main/
│           ├── java/                        ← Kotlin code lives here
│           │   └── com/yourdomain/deepseekwidget/
│           │       └── DeepSeekWidgetProvider.kt
│           ├── res/                         ← All resources live here
│           │   ├── drawable/
│           │   │   ├── ic_deepseek_whale.xml
│           │   │   ├── ic_mic.xml
│           │   │   └── widget_background.xml
│           │   ├── layout/
│           │   │   └── deepseek_widget.xml
│           │   ├── values/
│           │   │   └── themes.xml
│           │   └── xml/
│           │       └── deepseek_widget_info.xml
│           └── AndroidManifest.xml
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts                         ← Project-level
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

---

## ⚡ Build Locally

```bash
# Clone the repo
git clone https://github.com/rajit2004/DeepSeekWidget.git

# Open in Android Studio and let Gradle sync
# Then either:
#   Build APK → Build → Build Bundle(s)/APK → Build APK
#   Or run directly on a connected device

# CLI build
./gradlew assembleDebug
```

Output APK: `app/build/outputs/apk/debug/`

For a signed release APK (like the one on GitHub Releases), use:  
**Build → Generate Signed Bundle / APK → APK → Create new keystore** and follow the steps.

---

## 📋 Changelog

### v1.0 — First Release *(Pre-release)*
- ✅ One-tap home screen widget to open DeepSeek
- ✅ Voice input button with trampoline activity for voice recognition
- ✅ Material You design with DeepSeek teal accent
- ✅ Supports Android 8.0 and above
- ✅ Signed APK release

Full commit history: [commits/v1.0](https://github.com/rajit2004/DeepSeekWidget/commits/v1.0)

---

## 🤝 Contributing

This project is an open contribution to the DeepSeek ecosystem. If you'd like to help improve it:

1. Fork the repo
2. Create a feature branch — `git checkout -b feature/your-idea`
3. Commit your changes — `git commit -m "Add: your feature"`
4. Push and open a Pull Request

All contributions, issues, and feature suggestions are welcome.

---

## 💖 Support This Project

Everything here is free and open source. If this inspires your own project or saves you time:

[![Sponsor](https://img.shields.io/badge/Sponsor-%E2%9D%A4-pink?style=for-the-badge&logo=githubsponsors)](https://github.com/sponsors/rajit2004)

---

## 👨‍💻 Author

**Ranesh Rajit** — B.Tech CS Student, India

[![GitHub](https://img.shields.io/badge/GitHub-rajit2004-black?style=flat&logo=github)](https://github.com/rajit2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ranesh--kun-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ranesh-kun)

---

## 📄 License

Licensed under the [MIT License](LICENSE) — see the `LICENSE` file for details.  
Free to use, modify, and distribute with attribution.

> This is an independent open‑source project and is not officially affiliated with or endorsed by DeepSeek.
