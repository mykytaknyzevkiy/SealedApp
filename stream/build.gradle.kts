import AppDependencies.androidProject
import com.android.ide.common.util.pathTreeMapOf

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 26
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++14")
                arguments("-DANBOX_STREAM_SDK_DIR=" + "/Users/nekbakhtzabirov/Documents/SealedApp/stream/src/main/cpp/anbox")
            }
        }

        ndk {
            version = "21.4.7075529"
            abiFilters.apply {
                add("arm64-v8a")
                add("x86_64")
            }
        }
    }

    externalNativeBuild {
        cmake {
            version = "3.18.1"
            path = File("${projectDir}/src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

androidProject()

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client-jdk:1.12")
}