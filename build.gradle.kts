// Root build.gradle.kts
// AGP bumped from 8.2.0 → 8.7.3 (first version with full compileSdk 35 support)
// Kotlin bumped from 1.9.0 → 2.0.0  (recommended companion for AGP 8.5+)
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
}