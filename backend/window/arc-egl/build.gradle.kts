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
            api(projects.arcGlfw)
        }
        desktopMain.dependencies {
            implementation(libs.lwjgl.egl)
        }
    }
}

dependencies {
    lwjglDesktop(libs.lwjgl.glfw)
}
