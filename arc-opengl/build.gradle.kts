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
    "org.lwjgl:lwjgl-opengl",
)

dependencies {
    api(project(":arc-common"))

    // Natives for every OS
    natives.forEach { name ->
        implementation("$name:$lwjglVersion")
        implementation("$name:$lwjglVersion:$winNatives")
        implementation("$name:$lwjglVersion:$linuxNatives")
        implementation("$name:$lwjglVersion:$macosNatives")
        implementation("$name:$lwjglVersion:$macosArmNatives")
    }

    implementation("commons-io:commons-io:2.18.0")
}