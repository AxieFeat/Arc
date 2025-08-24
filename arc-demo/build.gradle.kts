dependencies {
    implementation(projects.arcOpengl)
    implementation(projects.arcVulkan)

    implementation(projects.arcExtensions.arcProfiler.arcProfilerSimple)
    implementation(projects.arcExtensions.arcDisplay.arcDisplayOpengl)
    implementation(projects.arcExtensions.arcAudio.arcAudioOpenal)
    implementation(projects.arcExtensions.arcModel.arcModelSimple)
    implementation(projects.arcExtensions.arcFont.arcFontSimple)
    implementation(projects.arcExtensions.arcInput.arcInputGlfw)

    // YOU MUSTN'T IMPLEMENT LWJGL IN REAL PROJECT, IT'S ONLY FOR TESTING FEATURES
    lwjgl(libs.lwjgl.lib)
    lwjgl(libs.lwjgl.glfw)
    lwjgl(libs.lwjgl.openal)
    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.opengl)

    implementation("de.articdive:jnoise-pipeline:4.1.0")
    implementation(libs.fastutil)
}