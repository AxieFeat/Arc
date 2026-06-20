plugins {
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

dependencies {
    api(projects.arcCore)

    lwjgl(libs.lwjgl.lib)
    lwjgl(libs.lwjgl.glfw)

    implementation(libs.fastutil)
    implementation(libs.oshi)
}
