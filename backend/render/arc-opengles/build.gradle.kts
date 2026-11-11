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
            implementation(projects.arcEgl)
            implementation(libs.commonsIo)

            lwjglDesktop(libs.lwjgl.stb)
            lwjglDesktop(libs.lwjgl.opengles)
            lwjglDesktop(libs.lwjgl.glfw)
        }
    }
}
