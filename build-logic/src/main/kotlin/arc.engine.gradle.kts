/**
 * Simple Gradle convention plugin for Arc Engine modules.
 * Applies common settings like Kotlin JVM target, KSP plugin, annotation processor setup,
 * and publishing configuration.
 *
 * This plugin applies in root project for all subprojects, but it skips projects, where
 * property "usePlugin" is set to "false".
 */

plugins {
    kotlin("jvm")
    `java-library`
}

kotlin {
    jvmToolchain(
        rootProject.findProperty("javaVersion").toString().toIntOrNull()
            ?: throw IllegalStateException("Java version not specified")
    )
}

applyAnnotationProcessor()
applyPublishing()
applyDetect()
