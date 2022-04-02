plugins {
    `kotlin-dsl`
}
repositories.jcenter()

sourceSets.main {
    java {
        setSrcDirs(setOf(projectDir.parentFile.resolve("src/main/kotlin")))
        include("Version.kt")
        include("AppDependencies.kt")
        include("Config.kt")
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.8")
}