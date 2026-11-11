plugins {
    id("arc.multiplatform")
    id("arc.publishing")
//    id("arc.detekt") TODO enable detekt, when got stable
    id("arc.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.arcCommon)
        }
        desktopMain.dependencies {
            implementation(projects.arcGlfw)

            // TODO Remove
            implementation("org.tinylog:tinylog-api:2.4.1")
            implementation("org.tinylog:tinylog-impl:2.4.1")

            // Natives of Vulkan exist only for macOS.
            lwjglDesktop(libs.lwjgl.vulkan, LwjglPlatform.MACOS_ARM64, LwjglPlatform.MACOS)

            lwjglDesktop(libs.lwjgl.shaderc)
            lwjglDesktop(libs.lwjgl.glfw)
            lwjglDesktop(libs.lwjgl.vma)
            lwjglDesktop(libs.lwjgl.stb)
            lwjglDesktop(libs.lwjgl.assimp)
        }
    }
}
