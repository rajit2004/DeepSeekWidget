# 🐳 DeepSeekWidget

> An open-source Android home screen widget for one-tap access to DeepSeek — ask anything, use voice, without unlocking the app.

[![Status](https://img.shields.io/badge/Status-In%20Development-orange?style=flat)](https://github.com/rajit2004/DeepSeekWidget)
[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)](https://github.com/rajit2004/DeepSeekWidget)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-7F52FF?style=flat&logo=kotlin)](https://github.com/rajit2004/DeepSeekWidget)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=flat&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)

---

## 📌 About This Project

This is an **open-source contribution** to the DeepSeek community — a feature request and working proof-of-concept for an official Android home screen widget for the DeepSeek app.

The goal is simple: eliminate the friction of opening an AI assistant. One tap on the widget, and you're in the chat. No unlocking, no navigation, no delay.

> ⚠️ **This project is currently under active development.** It is not yet released or available for download. Contributions, feedback, and feature suggestions are welcome.

---

## 🚀 Planned Features

- 🏠 **One-tap chat access** — open DeepSeek directly from the home screen widget
- 🎤 **Voice button** — trigger voice input instantly, no keyboard required
- 🎨 **Material You design** — adapts automatically to light/dark system theme
- 🐋 **DeepSeek branding** — uses the iconic whale and teal accent
- 📦 **Lightweight** — zero background services, minimal permissions
- 🔧 **Extensible** — easy to add custom prompt buttons or deep-links
- 🕊️ **Open source** — built for the community, free to fork and modify

---

## 🧠 How It Works

```
User taps widget on home screen
        ↓
PendingIntent fires → DeepSeek app or web opens
        ↓
User asks question → AI responds instantly

(Optional) Tap mic button → voice intent fires immediately
```

**Widget Logic:**
- Built as a standard `AppWidgetProvider` using `RemoteViews`
- Click listeners attach `PendingIntent`s for open-chat and voice-command actions
- No background processes — purely a shortcut layer

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
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
│   ├── src/main/
│   │   ├── java/com/yourdomain/deepseekwidget/
│   │   │   └── DeepSeekWidgetProvider.kt
│   │   ├── res/
│   │   │   ├── drawable/         # ic_deepseek_whale, ic_mic, widget_background
│   │   │   ├── layout/           # deepseek_widget.xml
│   │   │   ├── values/           # themes.xml
│   │   │   └── xml/              # deepseek_widget_info.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── screenshots/
├── build.gradle.kts
├── settings.gradle.kts
├── .gitignore
└── README.md
```

---

## ⚡ Build Locally

```bash
# Clone the repo
git clone https://github.com/rajit2004/DeepSeekWidget.git

# Open in Android Studio and let Gradle sync
# Then either:
#   Build APK → Build → Build Bundle(s)/APK → Build APK
#   Or run directly on a connected device via Run

# CLI build
./gradlew assembleDebug
```

Output APK: `app/build/outputs/apk/debug/`

---

## 🤝 Contributing

This project is an open contribution to the DeepSeek ecosystem. If you'd like to help build it:

1. Fork the repo
2. Create a feature branch — `git checkout -b feature/your-idea`
3. Commit your changes — `git commit -m "Add: your feature"`
4. Push and open a Pull Request

All contributions, issues, and feature requests are welcome.

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

Licensed under the [MIT License](LICENSE). Free to use, modify, and distribute with attribution.

---

> *This is an independent open-source project and is not officially affiliated with or endorsed by DeepSeek.*
