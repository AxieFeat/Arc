import arc.util.LwjglPlatform
import arc.util.lwjgl

dependencies {
    api(projects.arcCommon)

    // Natives of Vulkan exist only for macOS.
    lwjgl(libs.lwjgl.vulkan, LwjglPlatform.MACOS_ARM64, LwjglPlatform.MACOS)

    lwjgl(libs.lwjgl.shaderc)
    lwjgl(libs.lwjgl.glfw)
    lwjgl(libs.lwjgl.vma)
}