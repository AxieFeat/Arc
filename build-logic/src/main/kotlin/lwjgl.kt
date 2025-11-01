import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.api.artifacts.MinimalExternalModuleDependency

enum class LwjglPlatform(val classifier: String) {
    WINDOWS("natives-windows"),
    LINUX("natives-linux"),
    MACOS("natives-macos"),
    MACOS_ARM64("natives-macos-arm64")
}

/**
 * This function allows adding LWJGL dependencies along with their platform-specific natives.
 *
 * @param depend The LWJGL dependency to add.
 * @param platform The platforms for which to include natives. Defaults to all supported platforms.
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