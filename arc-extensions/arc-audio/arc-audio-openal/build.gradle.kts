import arc.util.lwjgl

dependencies {
    api(projects.arcExtensions.arcAudio.arcAudioCore)

    lwjgl(libs.lwjgl.openal)
    lwjgl(libs.lwjgl.stb)
}