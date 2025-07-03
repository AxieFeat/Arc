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

include("arc-annotations")
include("arc-annotation-processor")
include("arc-core")
include("arc-common")
include("arc-opengl")
include("arc-vulkan")

include("arc-extensions")
extension("arc-font",         "core", "simple")
extension("arc-model",        "core", "simple")
extension("arc-display",      "core", "common", "opengl")
extension("arc-audio",        "core", "openal")
extension("arc-profiler",     "core", "simple")
extension("arc-input",        "core", "glfw")

include("arc-demo")

fun extension(name: String, vararg impl: String) {
    include("arc-extensions:$name")

    impl.forEach {
        include("arc-extensions:$name:$name-$it")
    }
}