/**
 * Base Gradle convention plugin for modules.
 */

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(
        rootProject.findProperty("javaVersion").toString().toIntOrNull()
            ?: throw IllegalStateException("Java version not specified")
    )
}

tasks.test {
    useJUnitPlatform()
}


