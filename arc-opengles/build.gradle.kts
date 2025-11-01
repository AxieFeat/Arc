dependencies {
    api(projects.arcCommon)

    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.opengles)
    lwjgl(libs.lwjgl.glfw)
    implementation(libs.lwjgl.egl)

    implementation(libs.commonsIo)
}