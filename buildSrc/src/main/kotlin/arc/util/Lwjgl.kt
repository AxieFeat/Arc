package arc.util

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.api.artifacts.MinimalExternalModuleDependency

enum class LwjglPlatform(val classifier: String) {
    WINDOWS("natives-windows"),
    LINUX("natives-linux"),
    MACOS("natives-macos"),
    MACOS_ARM64("natives-macos-arm64")
}

fun DependencyHandler.lwjgl(depend: Provider<MinimalExternalModuleDependency>, vararg platform: LwjglPlatform = LwjglPlatform.values()) {
    add("implementation", depend)

    platform.forEach {
        add(depend, it)
    }
}

private fun DependencyHandler.add(depend: Provider<MinimalExternalModuleDependency>, platform: LwjglPlatform) {
    add("implementation", variantOf(depend) { classifier(platform.classifier) })
}