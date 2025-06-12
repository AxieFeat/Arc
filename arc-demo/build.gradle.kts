dependencies {
    implementation(projects.arcOpengl)
    implementation(projects.arcVulkan)

    implementation(projects.arcExtensions.arcProfiler.arcProfilerCommon)
    implementation(projects.arcExtensions.arcDisplay.arcDisplayOpengl)
    implementation(projects.arcExtensions.arcAudio.arcAudioOpenal)
    implementation(projects.arcExtensions.arcModel.arcModelCommon)
    implementation(projects.arcExtensions.arcFont.arcFontCommon)
    implementation(projects.arcExtensions.arcInput.arcInputGlfw)

    lwjgl(libs.lwjgl.lib)
    lwjgl(libs.lwjgl.glfw)
    lwjgl(libs.lwjgl.openal)
    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.opengl)

    implementation("de.articdive:jnoise-pipeline:4.1.0")
}