import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

object AppDependencies {

    private const val ktx = "androidx.core:core-ktx:${Version.ktx}"
    private const val kotlin_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.kotlin_coroutines}"

    const val gson = "com.google.code.gson:gson:${Version.gson}"

    val kotlinUI = arrayListOf<String>().apply {
        add("androidx.compose.ui:ui:${Version.compose}")
        // Tooling support (Previews, etc.)
        add( "androidx.compose.ui:ui-tooling:${Version.compose}")
        // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
        add( "androidx.compose.foundation:foundation:${Version.compose}")
        // Material Design
        add( "androidx.compose.material:material:${Version.compose}")

        add ("androidx.compose.material:material-icons-extended:${Version.compose}")

        add("com.google.accompanist:accompanist-swiperefresh:0.14.0")

        add("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02")

        add("io.coil-kt:coil-compose:1.4.0")

        add("io.coil-kt:coil-gif:1.4.0")


        add("com.google.accompanist:accompanist-pager:0.21.0-beta")
    }

    fun DependencyHandler.implementation(list: List<String>) {
        list.forEach { dependency ->
            add("implementation", dependency)
        }
    }

    fun DependencyHandlerScope.commonDependencies() {
        "implementation"(ktx)
        "implementation"(kotlin_coroutines)
        impTester()
    }

    fun Project.androidProject() {
        dependencies {
            "implementation"(project(":loger"))
            commonDependencies()
        }
    }

    private fun DependencyHandler.impTester() {
        this.add("testImplementation", "junit:junit:4.12")
        this.add("androidTestImplementation", "androidx.test.ext:junit-ktx:1.1.3")
        this.add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.4.0")
    }
}
