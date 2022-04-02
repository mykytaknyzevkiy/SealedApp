import AppDependencies.androidProject
import AppDependencies.implementation
import java.io.ByteArrayOutputStream

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.google.firebase.crashlytics")
}

android {
    signingConfigs {
        /*create("release") {
            storeFile = file("/Users/mykyta/Documents/MHP/mhp-android-app/key-store")
            storePassword = "12345678"
            keyAlias = "nz"
            keyPassword = "12345678"
        }*/
    }

    buildFeatures {
        compose = true
    }

    compileSdk = Version.compileSdk
    buildToolsVersion = Version.buildTool

    defaultConfig {
        applicationId = Config.packageName
        minSdk = Config.minSdkVersion.toInt()
        targetSdk = Config.targetVersion.toInt()
        versionCode = Config.versionCode
        versionName = Config.versionName
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "1.8"
        //useIR = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = false

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )

            applicationVariants.all {
                outputs.forEach { output ->
                    if (output is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                        output.outputFileName =
                                "sealed-app-debug-v${versionName}.${output.outputFile.extension}"
                    }
                }
            }

            firebaseAppDistribution {
                //artifactType = "AAB"
                releaseNotes = Config.releaseNote
                this.groups  = "internal"
            }
        }
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

androidProject()

dependencies {
    implementation (AppDependencies.kotlinUI)

    implementation("androidx.appcompat:appcompat:1.4.1")

    implementation ("androidx.fragment:fragment-ktx:1.4.1")

    implementation ( "androidx.compose.runtime:runtime-livedata:${Version.compose}")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    implementation ( "androidx.activity:activity-compose:1.4.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")

    implementation (platform("com.google.firebase:firebase-bom:28.4.2"))
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation( "com.google.firebase:firebase-crashlytics-ktx")

    implementation ("com.google.android.material:material:1.5.0")
}

tasks.register("deployForTester") {
    val slack = Slack()

    dependsOn("clean")

    if (gitHasChanges()) {
        throw Exception("pls make commit")
    }

    try {
        createReleaseBranch()
    } catch (e: Exception) {
        println(e)
        throw Exception("pls up version code")
    }

    dependsOn("assembleRelease")

    dependsOn("appDistributionUploadRelease")

    dependsOn("clean")

    doLast {
        println("DONE")

        // slack.sendUpdateNotification()
    }
}

fun gitHasChanges(): Boolean {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine = "git status --porcelain".split(" ")
        standardOutput = stdout
    }
    return stdout.toString().isNotEmpty()
}

fun createReleaseBranch() {
    // val currentDate: String = SimpleDateFormat("dd_MM_yyyy").format(Date())

    rootProject.exec {
        commandLine = "git tag ${Config.versionName}-${Config.versionCode}".split(" ")
    }

    rootProject.exec {
        commandLine = "git push --tags".split(" ")
    }

}