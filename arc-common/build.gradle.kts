plugins {
    id("arc.multiplatform")
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.arcCore)
        }
        desktopMain.dependencies {
            implementation(libs.fastutil)
            implementation(libs.oshi)
            implementation(libs.jspecify)

            lwjglDesktop(libs.lwjgl.lib)
        }
    }
}
