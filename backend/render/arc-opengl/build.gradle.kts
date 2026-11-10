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
            implementation(projects.arcGlfw)
            implementation(libs.commonsIo)
        }
    }
}

dependencies {
    lwjglDesktop(libs.lwjgl.stb)
    lwjglDesktop(libs.lwjgl.opengl)
    lwjglDesktop(libs.lwjgl.glfw)
}
