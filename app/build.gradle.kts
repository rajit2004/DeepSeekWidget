import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        load(FileInputStream(keystorePropertiesFile))
    }
}

android {
    namespace = "com.yourdomain.deepseekwidget"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yourdomain.deepseekwidget"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "2.0"
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
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.create("release") {
                    storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                    storePassword = keystoreProperties["storePassword"] as String
                    keyAlias = keystoreProperties["keyAlias"] as String
                    keyPassword = keystoreProperties["keyPassword"] as String
                }
            } else {
                signingConfigs.getByName("debug")
            }
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
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}