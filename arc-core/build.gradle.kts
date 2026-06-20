plugins {
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

dependencies {
    api(projects.arcAnnotations)

    api(libs.joml)
    api(libs.gson)
}
