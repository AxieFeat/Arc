@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "arc"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    includeBuild("build-logic")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

project("annotations")
project("annotation-processor")
project("core")
project("common")
project("opengl")
project("opengles")
project("vulkan")
project("native")

project("extensions")
extension("font",         "core", "simple")
extension("model",        "core", "simple")
extension("display",      "core", "common", "opengl")
extension("audio",        "core", "openal")
extension("profiler",     "core", "simple")
extension("input",        "core", "glfw")

project("demo")

fun project(module: String) {
    include("${rootProject.name}-$module")
}

fun extension(name: String, vararg impl: String) {
    val extension = "${rootProject.name}-extensions:${rootProject.name}-$name"
    include(extension)

    impl.forEach {
        include("$extension:${rootProject.name}-$name-$it")
    }
}