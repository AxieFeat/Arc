enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "arc"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
        gradlePluginPortal()
        repositories {
            maven {
                name = "Ci-Cd"
                url = uri("https://gitlab.qubique.pw/api/v4/groups/4/-/packages/maven")
                credentials(HttpHeaderCredentials::class) {
                    value = System.getenv("CI_JOB_TOKEN").also {
                        name = "Job-Token"
                    } ?: System.getenv("QUBIQUE_GITLAB_PRIVATE_TOKEN").also {
                        name = "Private-Token"
                    } ?: throw RuntimeException("Token not found!")
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
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
extension("arc-font",         "core", "common")
extension("arc-model",        "core", "common")
extension("arc-display",      "core", "common", "opengl")
extension("arc-audio",        "core", "openal")
extension("arc-profiler",     "core", "common")
extension("arc-input",        "core", "glfw")

include("arc-demo")

fun extension(name: String, vararg impl: String) {
    include("arc-extensions:$name")

    impl.forEach {
        include("arc-extensions:$name:$name-$it")
    }
}
