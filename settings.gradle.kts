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
include("arc-extensions:arc-profiler")
findProject(":arc-extensions:arc-profiler")?.name = "arc-profiler"
include("arc-demo")
