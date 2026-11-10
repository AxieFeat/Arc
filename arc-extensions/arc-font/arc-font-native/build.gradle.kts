plugins {
    id("arc.multiplatform")
    id("arc.publishing")
    id("arc.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.arcExtensions.arcFont.arcFontCore)
        }
        desktopMain.dependencies {
            implementation(libs.lwjgl.harfbuzz)
        }
    }
}

dependencies {
    lwjglDesktop(libs.lwjgl.lib)
    lwjglDesktop(libs.lwjgl.freetype)
}
