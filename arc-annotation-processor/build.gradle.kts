plugins {
    id("arc.base")
}

dependencies {
    implementation(projects.arcAnnotations)
    implementation(libs.ksp)
    implementation(libs.annotations)
}
