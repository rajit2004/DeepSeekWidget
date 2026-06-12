# Security Policy

## Supported Versions

DeepSeekWidget is currently in active development. Security updates are provided for the latest release only.

| Version | Supported |
| ------- | --------- |
| 1.2.x   | ✅        |
| < 1.2   | ❌        |

## Security Features Built into DeepSeekWidget

This widget is designed with privacy and security as core principles. Here's what's already implemented:

- **Zero data collection** – No analytics, no telemetry, no cloud calls. The widget is a pure router.
- **Runtime permissions** – `CAMERA` and `RECORD_AUDIO` are requested only when the user taps the respective button, never in the background.
- **FileProvider** – Camera images are shared using Android's `FileProvider` with scoped storage. No raw file paths are exposed to other apps.
- **Intent security** – All `PendingIntent`s use `FLAG_IMMUTABLE` (Android 12+ compliant). Unique request codes per widget instance prevent intent collision.
- **No background services** – The widget has zero background processes. It only wakes when tapped, so no battery drain and no unexpected data transmission.
- **Local‑only storage** – Any temporary files (e.g., camera images) are stored in the app's private external storage directory and deleted after use.

## Reporting a Vulnerability

If you discover a security vulnerability in DeepSeekWidget, please report it responsibly.

**Do not open a public GitHub issue** for security‑sensitive reports.

Instead, contact the maintainer directly via email:

📧 **raneshrajit@gmail.com**

Please include:

- A clear description of the vulnerability
- Steps to reproduce (if applicable)
- Potential impact
- Any suggested fix (optional)

You can expect an initial response within **48 hours**. Once the issue is confirmed, a fix will be prepared for the next release, and you will be credited in the release notes (unless you prefer to remain anonymous).

## Disclosure Process

1. Report the vulnerability via email.
2. The maintainer will validate the report and may ask for additional details.
3. A fix is developed and tested in a private branch.
4. A new version is released, and the vulnerability is publicly disclosed in the release notes (with credit to the reporter).

## Best Practices for Users

- **Only download the APK from the official GitHub Releases page** – https://github.com/rajit2004/DeepSeekWidget/releases
- Verify the SHA‑256 checksum if provided in the release notes.
- Keep the widget updated to the latest version.
- The widget requires the DeepSeek app or a web browser to function. Ensure those are obtained from official sources.

## Third‑Party Dependencies

DeepSeekWidget uses only standard AndroidX libraries and the Kotlin standard library. No third‑party networking, analytics, or crash‑reporting SDKs are included.

| Dependency | Purpose | Security notes |
|------------|---------|----------------|
| AndroidX Core | Android framework utilities | Official Google libraries |
| AndroidX AppCompat | Backward compatibility | Official Google libraries |
| Material Components | UI theming | Official Google libraries |

## Contact

For non‑security questions, please use GitHub Issues or Discussions.

**Maintainer:** Ranesh Rajit  
**GitHub:** https://github.com/rajit2004  
**Email:** raneshrajit@gmail.com
