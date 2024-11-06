plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    val kotlinVersion by extra("1.8.0") // Set Kotlin version here
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.0") // Use the appropriate version for your project
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.4.2")
    }
}
