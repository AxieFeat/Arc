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
    }
}

dependencies {
    lwjglDesktop(libs.lwjgl.glfw)
}
