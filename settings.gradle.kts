rootProject.name = "arc"

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

include("arc-core")
include("arc-annotations")
include("arc-vulkan")
include("arc-opengl")
include("arc-common")
include("arc-extensions")
include("arc-extensions:arc-profiler")
include("arc-demo")
include("arc-extensions:arc-font")
include("arc-extensions:arc-model")
include("arc-extensions:arc-display")
include("arc-extensions:arc-audio")
include("arc-extensions:arc-audio:arc-audio-core")
include("arc-extensions:arc-audio:arc-audio-openal")
include("arc-extensions:arc-display:arc-display-common")
include("arc-extensions:arc-display:arc-display-core")
include("arc-extensions:arc-display:arc-display-opengl")
include("arc-extensions:arc-font:arc-font-core")
include("arc-extensions:arc-font:arc-font-common")
include("arc-extensions:arc-model:arc-model-core")
include("arc-extensions:arc-model:arc-model-common")
include("arc-extensions:arc-profiler:arc-profiler-core")
include("arc-extensions:arc-profiler:arc-profiler-common")
include("arc-annotation-processor")
