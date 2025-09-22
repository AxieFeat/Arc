import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.api.artifacts.MinimalExternalModuleDependency

/**
 * This enum represents the different LWJGL platforms and their corresponding classifiers.
 */
enum class LwjglPlatform(val classifier: String) {
    WINDOWS("natives-windows"),
    LINUX("natives-linux"),
    MACOS("natives-macos"),
    MACOS_ARM64("natives-macos-arm64")
}

/**
 * With this function, you can easily add LWJGL dependencies for multiple platforms.
 * By default, it includes all platforms, but you can specify a subset if needed.
 *
 * @param depend The LWJGL dependency to add.
 * @param platform Platforms for native libraries. Defaults to all platforms.
 */
fun DependencyHandler.lwjgl(depend: Provider<MinimalExternalModuleDependency>, vararg platform: LwjglPlatform = LwjglPlatform.values()) {
    add("implementation", depend)

    platform.forEach {
        add(depend, it)
    }
}

private fun DependencyHandler.add(depend: Provider<MinimalExternalModuleDependency>, platform: LwjglPlatform) {
    add("implementation", variantOf(depend) { classifier(platform.classifier) })
}