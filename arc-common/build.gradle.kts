import arc.util.lwjgl
import org.gradle.kotlin.dsl.dependencies

dependencies {
    api(projects.arcCore)

    lwjgl(libs.lwjgl.lib)
    lwjgl(libs.lwjgl.glfw)

    implementation(libs.fastutil)
    implementation(libs.oshi)
}