# Contributing to DeepSeekWidget

Thanks for taking the time to contribute. This is a small open-source project — contributions of any size are welcome.

---

## Before You Start

- Check [open issues](https://github.com/rajit2004/DeepSeekWidget/issues) to see if your idea or bug is already tracked
- For big changes, open an issue first so we can discuss before you invest time building

---

## How to Contribute

### Reporting a Bug

Open an issue with:
- Device model and Android version
- Steps to reproduce
- What you expected vs what happened
- Logcat output if available (`adb logcat | grep DeepSeekWidget`)

### Suggesting a Feature

Open an issue with:
- What you want and why
- How it fits the widget's goal (zero-friction access to DeepSeek)
- Any reference implementation (e.g. how ChatGPT or Gemini widgets do it)

### Submitting a Pull Request

```bash
# 1. Fork and clone
git clone https://github.com/YOUR_USERNAME/DeepSeekWidget.git
cd DeepSeekWidget

# 2. Create a branch
git checkout -b feature/your-feature-name
# or
git checkout -b fix/bug-description

# 3. Make your changes, then build and test on a real device
./gradlew assembleDebug

# 4. Commit
git commit -m "Add: short description of what changed"

# 5. Push and open a PR against main
git push origin feature/your-feature-name
```

---

## Code Style

- Kotlin only — no Java
- Follow existing naming conventions (`camelCase` for functions, `PascalCase` for classes)
- All shared constants go in `Constants.kt` — never hardcode package names or URIs elsewhere
- Every new method that isn't obvious needs a one-line KDoc comment
- No dead code, no commented-out blocks in PRs

---

## Commit Message Format

```
Add: short description       ← new feature
Fix: short description       ← bug fix
Refactor: short description  ← code change with no behavior change
Docs: short description      ← README, comments, CHANGELOG only
Bump: dependency or SDK      ← version updates
```

---

## What Makes a Good PR

- Solves one thing — don't bundle multiple changes
- Builds without warnings on `./gradlew assembleDebug`
- Tested on a real device (emulator results are secondary — widget behavior differs)
- Updates `CHANGELOG.md` under an `Unreleased` section
- Updates `README.md` if the feature is user-facing

---

## Areas That Need Help

| Area | What's needed |
|---|---|
| Deep link research | Test whether `deepseek://chat` and `deepseek://voice` are handled by the DeepSeek app |
| OEM testing | Test on MIUI, EMUI, ColorOS — widget behavior varies heavily on custom launchers |
| Widget resize | Make the layout adapt gracefully when resized to 4×1 or 2×1 cells |
| Accessibility | Add proper TalkBack support for all three tap targets |
| Preview image | Create a proper `preview_widget.png` for the Android widget picker |

---

## License

By contributing, you agree your changes are released under the [MIT License](LICENSE).
