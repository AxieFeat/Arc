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

    api("it.unimi.dsi:fastutil:8.5.15")

    // GLFW api
    api("org.lwjgl:lwjgl-glfw:$lwjglVersion")

    // Natives for every OS
    natives.forEach { name ->
        api("$name:$lwjglVersion")
        api("$name:$lwjglVersion:$winNatives")
        api("$name:$lwjglVersion:$linuxNatives")
        api("$name:$lwjglVersion:$macosNatives")
        api("$name:$lwjglVersion:$macosArmNatives")
    }

    // Input handle
    api("net.java.jinput:jinput:2.0.10")
    api("net.java.jinput:jinput:2.0.10:natives-all")
    api("net.java.jinput:jinput-platform:2.0.7:natives-linux")
    api("net.java.jinput:jinput-platform:2.0.7:natives-osx")
    api("net.java.jinput:jinput-platform:2.0.7:natives-windows")

    api("com.github.kwhat:jnativehook:2.2.2")

    api("com.github.oshi:oshi-core:6.6.6")


}