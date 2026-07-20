plugins {
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

dependencies {
    api(projects.arcCommon)
    api(projects.arcGlfw)

    lwjgl(libs.lwjgl.glfw)
    implementation(libs.lwjgl.egl)
}
