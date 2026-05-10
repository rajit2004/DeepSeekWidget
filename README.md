# 🐳 DeepSeekWidget

> One-tap Android home screen widget for instant DeepSeek AI access — ask anything, use voice, no unlocking needed.

[![Release](https://img.shields.io/github/v/release/rajit2004/DeepSeekWidget?style=for-the-badge&logo=android&color=00D4AA)](https://github.com/rajit2004/DeepSeekWidget/releases)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/rajit2004?style=for-the-badge&logo=githubsponsors&color=EA4AAA)](https://github.com/sponsors/rajit2004)
[![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=flat&logo=android)](https://github.com/rajit2004/DeepSeekWidget)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-7F52FF?style=flat&logo=kotlin)](https://github.com/rajit2004/DeepSeekWidget)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)

---

## 📥 Download

[![Get APK](https://img.shields.io/badge/Download%20APK-Releases-blue?style=for-the-badge&logo=github)](https://github.com/rajit2004/DeepSeekWidget/releases)

No Play Store required. Download the latest APK from Releases, install on Android 8.0+, and add the widget to your home screen.

---

## 📸 Screenshots

| Widget on Home Screen | Tap to Open Chat |
|:---:|:---:|
| <img src="screenshots/widget_home.png" width="220"/> | <img src="screenshots/open_chat.png" width="220"/> |
| **Voice Button** | **Dark Mode** |
| <img src="screenshots/voice_button.png" width="220"/> | <img src="screenshots/dark_theme.png" width="220"/> |

> Replace placeholder images by adding your own files inside the `screenshots/` folder.

---

## 🚀 Features

- 🏠 **One-tap access** — open DeepSeek chat directly from the home screen widget
- 🎤 **Voice button** — start voice input instantly, no keyboard needed
- 🎨 **Material You design** — adapts automatically to light and dark themes
- 🐋 **Respectful branding** — uses DeepSeek's iconic whale and teal accent
- 📦 **Lightweight** — zero background services, no unnecessary permissions
- 🔧 **Customisable** — easy to extend with your own prompt buttons or deep-links
- 🕊️ **Open source** — minimal, hackable, built for the community

---

## 🧠 How It Works

```
User taps widget on home screen
        ↓
Intent fires → opens DeepSeek (app or web)
        ↓
User asks question → AI answers instantly

(Optional) Tap mic button → voice intent starts
```

**Under the hood:**
- Widget is a simple `AppWidgetProvider` using `RemoteViews`
- Click listeners attach `PendingIntent`s for open-chat and voice-command actions
- No background activity — the widget acts purely as a shortcut

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin 1.9+ |
| UI | XML (RemoteViews) |
| Build System | Gradle (KTS) |
| Min SDK | Android 8.0 (API 26) |
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
│   │   │   ├── drawable/        # ic_deepseek_whale, ic_mic, widget_background
│   │   │   ├── layout/          # deepseek_widget.xml
│   │   │   ├── values/          # themes.xml
│   │   │   └── xml/             # deepseek_widget_info.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── screenshots/
├── build.gradle.kts
├── settings.gradle.kts
├── .gitignore
└── README.md
```

---

## ⚡ Getting Started

### Prerequisites
- Android Studio (Hedgehog or later)
- Android device or emulator running API 26+

### Clone & Build

```bash
git clone https://github.com/rajit2004/DeepSeekWidget.git
cd DeepSeekWidget
```

Open in Android Studio, let Gradle sync, then:

**Via Android Studio:**
`Build → Build Bundle(s)/APK → Build APK`

**Via command line:**
```bash
./gradlew assembleDebug
```

Output APK: `app/build/outputs/apk/debug/`

### Install on Device

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or sideload the APK from the [Releases page](https://github.com/rajit2004/DeepSeekWidget/releases).

---

## 💖 Support This Project

Free and open source. If it saves you a few taps a day or inspires your own project:

[![Sponsor](https://img.shields.io/badge/Sponsor-%E2%9D%A4-pink?style=for-the-badge&logo=githubsponsors)](https://github.com/sponsors/rajit2004)

---

## 👨‍💻 Author

**Ranesh Rajit** — B.Tech CS Student, India

[![GitHub](https://img.shields.io/badge/GitHub-rajit2004-black?style=flat&logo=github)](https://github.com/rajit2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-ranesh--kun-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ranesh-kun)

---

## 📄 License

Licensed under the [MIT License](LICENSE). Free to use, modify, and distribute — just keep the original license and copyright notice.