plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.ksp)
    implementation(gradleApi())
}

kotlin {
    jvmToolchain(
        rootProject.findProperty("javaVersion").toString().toIntOrNull()
            ?: throw IllegalStateException("Java version not specified")
    )
}
