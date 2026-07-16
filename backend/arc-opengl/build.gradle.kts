plugins {
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

dependencies {
    api(projects.arcCommon)

    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.opengl)
    lwjgl(libs.lwjgl.glfw)

    implementation(libs.commonsIo)
}
