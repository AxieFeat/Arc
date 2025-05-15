import arc.util.lwjgl

dependencies {
    api(projects.arcCommon)

    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.opengl)

    implementation(libs.commonsIo)
}