plugins {
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

dependencies {
    api(projects.arcCore)
    api(libs.bundles.logging)

    lwjgl(libs.lwjgl.lib)

    implementation(libs.fastutil)
    implementation(libs.oshi)
}
