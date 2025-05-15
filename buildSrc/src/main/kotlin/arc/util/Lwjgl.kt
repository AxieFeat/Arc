package arc.util

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.api.artifacts.MinimalExternalModuleDependency

private enum class LwjglPlatform(val classifier: String) {
    WINDOWS("natives-windows"),
    LINUX("natives-linux"),
    MACOS("natives-macos"),
    MACOS_ARM64("natives-macos-arm64")
}

fun DependencyHandler.lwjgl(depend: Provider<MinimalExternalModuleDependency>) {
    add("implementation", depend)

    LwjglPlatform.values().forEach {
        add("implementation", variantOf(depend) { classifier(it.classifier) })
    }
}