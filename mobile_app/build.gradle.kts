// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.gradle)
        classpath(libs.hilt.android.gradle.plugin)
        // Ensure you keep your existing classpath dependencies
    }
}