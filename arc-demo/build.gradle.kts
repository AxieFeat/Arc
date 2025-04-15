dependencies {
    implementation(project(":arc-opengl"))
    implementation(project(":arc-vulkan"))
    implementation(project(":arc-extensions:arc-profiler"))
    implementation(project(":arc-extensions:arc-model"))
    implementation(project(":arc-extensions:arc-display"))

    implementation(project(":arc-extensions:arc-audio:arc-audio-openal"))
    implementation(project(":arc-extensions:arc-model:arc-model-common"))

    implementation("de.articdive:jnoise-pipeline:4.1.0")
}

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
    natives.forEach { name ->
        implementation("$name:$lwjglVersion")
        implementation("$name:$lwjglVersion:$winNatives")
        implementation("$name:$lwjglVersion:$linuxNatives")
        implementation("$name:$lwjglVersion:$macosNatives")
        implementation("$name:$lwjglVersion:$macosArmNatives")
    }
}