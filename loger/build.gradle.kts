import AppDependencies.commonDependencies

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = (Version.compileSdk)
    buildToolsVersion = (Version.buildTool)

    defaultConfig {
        minSdkPreview = (Config.minSdkVersion)
        targetSdk =  (Config.targetVersion.toInt())
    }
}

dependencies {
    commonDependencies()

    implementation (AppDependencies.gson)

    //implementation ("com.jakewharton.timber:timber:5.0.1")
}