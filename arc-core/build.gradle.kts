plugins {
    id("arc.multiplatform")
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.io)
            api(libs.kotlinx.datetime)
            api(libs.korlibs.math.vector)
            api(libs.ditchoom.buffer)
            api(projects.arcAnnotations)

            implementation(libs.kotlinx.json)
            implementation(libs.kotlinx.coroutines)
        }

        desktopMain.dependencies {
            api(libs.bundles.logging)
        }
    }
}
