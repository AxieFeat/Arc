rootProject.name = "arc"

pluginManagement {
    repositories {
        mavenCentral()
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
