plugins {
    id("org.jetbrains.dokka") version "2.0.0"
}

dependencies {
    api(projects.arcAnnotations)

    api(libs.joml)
    api(libs.gson)
}