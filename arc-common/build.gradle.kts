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
    implementation(project(":arc-extensions:arc-display"))
    implementation(project(":arc-extensions:arc-audio"))

    // Natives for every OS
    natives.forEach { name ->
        implementation("$name:$lwjglVersion")
        implementation("$name:$lwjglVersion:$winNatives")
        implementation("$name:$lwjglVersion:$linuxNatives")
        implementation("$name:$lwjglVersion:$macosNatives")
        implementation("$name:$lwjglVersion:$macosArmNatives")
    }

    implementation("it.unimi.dsi:fastutil:8.5.15")
    implementation("com.github.oshi:oshi-core:6.6.6")
}