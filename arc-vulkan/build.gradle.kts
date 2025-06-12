dependencies {
    api(projects.arcCommon)

    // Natives of Vulkan exist only for macOS.
    lwjgl(libs.lwjgl.vulkan, LwjglPlatform.MACOS_ARM64, LwjglPlatform.MACOS)

    lwjgl(libs.lwjgl.shaderc)
    lwjgl(libs.lwjgl.glfw)
    lwjgl(libs.lwjgl.vma)
    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.assimp)

    implementation("org.tinylog:tinylog-api:2.4.1")
    implementation("org.tinylog:tinylog-impl:2.4.1")
}