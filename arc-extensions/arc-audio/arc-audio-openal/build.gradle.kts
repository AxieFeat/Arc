import arc.util.lwjgl

dependencies {
    implementation(projects.arcCommon)
    api(projects.arcExtensions.arcAudio.arcAudioCore)

    lwjgl(libs.lwjgl.openal)
    lwjgl(libs.lwjgl.stb)
}