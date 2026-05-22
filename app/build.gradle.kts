plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.yourdomain.deepseekwidget"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yourdomain.deepseekwidget"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        // Required so VoiceInputActivity can use BuildConfig.APPLICATION_ID
        // for the FileProvider authority — keeps it in sync with applicationId.
        buildConfig = true
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            // R8 full-mode: shrinks code and resources, reducing APK from ~4.5 MB to ~1 MB.
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // NOTE: signingConfig is NOT set here intentionally.
            // The debug keystore must NOT be used for a public/Play Store release.
            // To sign a release APK:
            //   1. Generate a keystore: Build → Generate Signed Bundle/APK
            //   2. Store credentials in keystore.properties (already in .gitignore)
            //   3. Read them here via:
            //      val keystoreProps = Properties().apply { load(rootProject.file("keystore.properties").inputStream()) }
            //      signingConfig = signingConfigs.create("release") { ... }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
}