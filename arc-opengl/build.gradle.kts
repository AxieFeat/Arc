val lwjglVersion = "3.3.6"
val winNatives = "natives-windows"
val linuxNatives = "natives-linux"
val macosNatives = "natives-macos"
val macosArmNatives = "natives-macos-arm64"

private val natives = listOf(
    "org.lwjgl:lwjgl-opengl",
    "org.lwjgl:lwjgl-stb"
)

dependencies {
    api(project(":arc-common"))

    // Natives for every OS
    natives.forEach { name ->
        api("$name:$lwjglVersion")
        api("$name:$lwjglVersion:$winNatives")
        api("$name:$lwjglVersion:$linuxNatives")
        api("$name:$lwjglVersion:$macosNatives")
        api("$name:$lwjglVersion:$macosArmNatives")
    }

    implementation("commons-io:commons-io:2.18.0")
    implementation("com.google.code.gson:gson:2.12.1")
}