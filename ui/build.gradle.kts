import AppDependencies.androidProject
import AppDependencies.implementation

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    buildFeatures {
        compose = true
    }

    kapt {
        generateStubs = true
    }

    compileSdk = (Version.compileSdk)
    buildToolsVersion = (Version.buildTool)

    defaultConfig {
        minSdkPreview = (Config.minSdkVersion)
        targetSdk =  (Config.targetVersion.toInt())
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }
}

androidProject()

dependencies {
    implementation(AppDependencies.kotlinUI)
}