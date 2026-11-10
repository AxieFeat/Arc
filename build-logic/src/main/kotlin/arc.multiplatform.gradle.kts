plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvmToolchain(
        rootProject.findProperty("javaVersion").toString().toIntOrNull()
            ?: throw IllegalStateException("Java version not specified")
    )

    jvm("desktop")
    android {
        // Autogeneration namespace via gradle project path
        // :arc-core -> arc.engine.arc_core
        // :arc-extensions:arc-model:arc-model-core -> arc.engine.arc_extensions.arc_model.arc_model_core
        // ...etc
        namespace = "arc.engine." + project.path
            .removePrefix(":")
            .replace(':', '.')
            .replace('-', '_')
        compileSdk = 36
        minSdk = 24
    }
    iosArm64()
    iosSimulatorArm64()
    iosX64()

    sourceSets {

        configureEach {
            kotlin.setSrcDirs(listOf("$name/kotlin"))
            resources.setSrcDirs(listOf("$name/resources"))
        }
    }
}
