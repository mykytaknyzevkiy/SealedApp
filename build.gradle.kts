buildscript {
    allprojects {
        repositories {
            google()
            mavenCentral()
            maven { setUrl("https://jitpack.io") }
        }
    }

    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-appdistribution-gradle:3.0.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    delete(File("buildSrc/build"))
    delete(File("*/build"))
}