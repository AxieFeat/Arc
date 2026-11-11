plugins {
    id("arc.multiplatform")
    id("arc.publishing")
    id("arc.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.arcExtensions.arcInput.arcInputCore)
        }
        desktopMain.dependencies {
            lwjglDesktop(libs.lwjgl.glfw)
        }
    }
}
