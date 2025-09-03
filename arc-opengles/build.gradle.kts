dependencies {
    api(projects.arcCommon)

    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.opengles)
    lwjgl(libs.lwjgl.glfw)
    api(libs.lwjgl.egl)

    implementation(libs.commonsIo)
}