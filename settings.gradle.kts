@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "arc"
includeBuild("arc-native")

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS

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
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Main modules
project("annotations")
project("annotation-processor")
project("core")
project("common")
project("extra")

// Render backends
render("opengl")
render("opengles")
render("vulkan")

// Window backends
window("glfw")
window("egl")
window("sdl")

project("extensions")
extension("font",         "core", "simple")
extension("model",        "core", "simple")
extension("display",      "core", "common", "opengl")
extension("audio",        "core", "openal")
extension("profiler",     "core", "simple")
extension("input",        "core", "glfw")

project("demo")

/**
 * Function for including main subprojects.
 *
 * @param moduleName Name of subproject without project prefix.
 *
 * @return The final name of the subproject includes the project prefix.
 */
fun project(moduleName: String): String {
    return "${rootProject.name}-$moduleName".also { name -> include(name) }
}

/**
 * Function for including rendering backends subprojects.
 */
fun render(moduleName: String) {
    val module = project(moduleName)

    // We use `settings.project()` because has own `project` function.
    settings.project(":$module").projectDir = file("backend/render/$module")
}

/**
 * Function for including window backends subprojects.
 */
fun window(moduleName: String) {
    val module = project(moduleName)

    // We use `settings.project()` because has own `project` function.
    settings.project(":$module").projectDir = file("backend/window/$module")
}

fun extension(name: String, vararg impl: String) {
    val extension = "${rootProject.name}-extensions:${rootProject.name}-$name"
    include(extension)

    impl.forEach {
        include("$extension:${rootProject.name}-$name-$it")
    }
}
