val lwjglVersion = "3.3.6"
val winNatives = "natives-windows"
val linuxNatives = "natives-linux"
val macosNatives = "natives-macos"
val macosArmNatives = "natives-macos-arm64"

private val natives = listOf(
    "org.lwjgl:lwjgl",
    "org.lwjgl:lwjgl-glfw",
    "org.lwjgl:lwjgl-openal",
    "org.lwjgl:lwjgl-stb",
)

dependencies {
    api(project(":arc-core"))
    implementation(project(":arc-extensions:arc-profiler"))
    implementation(project(":arc-extensions:arc-model"))

    // Natives for every OS
    natives.forEach { name ->
        api("$name:$lwjglVersion")
        api("$name:$lwjglVersion:$winNatives")
        api("$name:$lwjglVersion:$linuxNatives")
        api("$name:$lwjglVersion:$macosNatives")
        api("$name:$lwjglVersion:$macosArmNatives")
    }

    api("it.unimi.dsi:fastutil:8.5.15")
    api("com.github.oshi:oshi-core:6.6.6")
}