plugins {
    id("arc.multiplatform")
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.arcCommon)
        }
        desktopMain.dependencies {
            lwjglDesktop(libs.lwjgl.glfw)
        }
    }
}
