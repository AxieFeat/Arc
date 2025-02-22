plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "arc"

include("arc-core")
include("arc-annotations")
include("arc-vulkan")
include("arc-opengl")
include("arc-common")
include("arc-extensions")
include("arc-extensions:arc-discord")
findProject(":arc-extensions:arc-discord")?.name = "arc-discord"
include("arc-extensions:arc-profiling")
findProject(":arc-extensions:arc-profiling")?.name = "arc-profiling"
include("arc-demo")
