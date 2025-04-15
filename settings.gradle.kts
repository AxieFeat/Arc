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
findProject(":arc-extensions:arc-profiler")?.name = "arc-profiler"
include("arc-demo")
include("arc-extensions:arc-font")
findProject(":arc-extensions:arc-font")?.name = "arc-font"
include("arc-extensions:arc-model")
findProject(":arc-extensions:arc-model")?.name = "arc-model"
include("arc-extensions:arc-display")
findProject(":arc-extensions:arc-display")?.name = "arc-display"
include("arc-extensions:arc-light")
findProject(":arc-extensions:arc-light")?.name = "arc-light"
include("arc-extensions:arc-audio")
findProject(":arc-extensions:arc-audio")?.name = "arc-audio"
include("arc-extensions:arc-audio:arc-audio-core")
findProject(":arc-extensions:arc-audio:arc-audio-core")?.name = "arc-audio-core"
include("arc-extensions:arc-audio:arc-audio-openal")
findProject(":arc-extensions:arc-audio:arc-audio-openal")?.name = "arc-audio-openal"
include("arc-extensions:arc-display:arc-display-common")
findProject(":arc-extensions:arc-display:arc-display-common")?.name = "arc-display-common"
include("arc-extensions:arc-display:arc-display-core")
findProject(":arc-extensions:arc-display:arc-display-core")?.name = "arc-display-core"
include("arc-extensions:arc-display:arc-display-opengl")
findProject(":arc-extensions:arc-display:arc-display-opengl")?.name = "arc-display-opengl"
include("arc-extensions:arc-font:arc-font-core")
findProject(":arc-extensions:arc-font:arc-font-core")?.name = "arc-font-core"
include("arc-extensions:arc-font:arc-font-opengl")
findProject(":arc-extensions:arc-font:arc-font-opengl")?.name = "arc-font-opengl"
include("arc-extensions:arc-light:arc-light-core")
findProject(":arc-extensions:arc-light:arc-light-core")?.name = "arc-light-core"
include("arc-extensions:arc-light:arc-light-opengl")
findProject(":arc-extensions:arc-light:arc-light-opengl")?.name = "arc-light-opengl"
include("arc-extensions:arc-model:arc-model-core")
findProject(":arc-extensions:arc-model:arc-model-core")?.name = "arc-model-core"
include("arc-extensions:arc-model:arc-model-common")
findProject(":arc-extensions:arc-model:arc-model-common")?.name = "arc-model-common"
include("arc-extensions:arc-profiler:arc-profiler-core")
findProject(":arc-extensions:arc-profiler:arc-profiler-core")?.name = "arc-profiler-core"
include("arc-extensions:arc-profiler:arc-profiler-common")
findProject(":arc-extensions:arc-profiler:arc-profiler-common")?.name = "arc-profiler-common"
