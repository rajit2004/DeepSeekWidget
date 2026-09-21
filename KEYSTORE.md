# Production Keystore Setup

This guide explains how to sign the release APK with a production keystore for Play Store distribution.

## Step 1: Generate a Keystore

```bash
keytool -genkey -v -keystore deepseek-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias deepseek
```

You'll be prompted for:
- Keystore password
- Your name, organization, city, state, country code
- Key password

## Step 2: Create keystore.properties

Copy `keystore.properties.example` to `keystore.properties` in the project root:

```bash
cp keystore.properties.example keystore.properties
```

Fill in your actual credentials:

```properties
storeFile=deepseek-release.jks
storePassword=your_store_password
keyAlias=deepseek
keyPassword=your_key_password
```

## Step 3: Build

The `app/build.gradle.kts` file will automatically detect `keystore.properties` and use it for release signing. If the file doesn't exist, the debug keystore is used as a fallback.

```bash
./gradlew assembleRelease
```

## Security Notes

- **Never commit `keystore.properties`** — it's already in `.gitignore`
- **Never commit `.jks` or `.keystore` files** — also in `.gitignore`
- Back up your keystore securely — losing it means you can't update your Play Store app
- Use different keystores for debug and release
