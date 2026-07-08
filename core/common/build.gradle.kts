import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.flixclusive.library)
    alias(libs.plugins.flixclusive.compose)
    alias(libs.plugins.flixclusive.hilt)
}

android {
    namespace = "com.flixclusive.core.common"

    defaultConfig {
        val token = try {
            val properties = Properties()
            properties.load(project.rootProject.file("local.properties").inputStream())
            properties.getProperty("GITHUB_TOKEN", "")
        } catch (_: Exception) {
            ""
        }

        buildConfigField("String", "GITHUB_TOKEN", "\"$token\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.stubs.util)
    implementation(libs.stubs.model.film)
    implementation(libs.stubs.model.provider)
    implementation(libs.okhttp)

    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
}
