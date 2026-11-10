plugins {
    id("arc.multiplatform")
    id("arc.publishing")
    id("arc.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.arcExtensions.arcAudio.arcAudioCore)
        }
    }
}

dependencies {
    lwjglDesktop(libs.lwjgl.openal)
    lwjglDesktop(libs.lwjgl.stb)
}
