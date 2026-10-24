dependencies {
    api(projects.arcExtensions.arcFont.arcFontCore)
    lwjgl(libs.lwjgl.lib)
    lwjgl(libs.lwjgl.freetype)
    implementation(libs.lwjgl.harfbuzz)
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.0")
}
