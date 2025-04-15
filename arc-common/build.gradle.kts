val lwjglVersion = "3.3.6"
val winNatives = "natives-windows"
val linuxNatives = "natives-linux"
val macosNatives = "natives-macos"
val macosArmNatives = "natives-macos-arm64"

private val natives = listOf(
    "org.lwjgl:lwjgl-glfw"
)

dependencies {
    api(project(":arc-core"))

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