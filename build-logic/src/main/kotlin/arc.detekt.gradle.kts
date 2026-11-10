import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for modules that should match with the code style of project.
 *
 * It will be skipped, if `skipDetekt` property is set to "true".
 */

if(!isDetektSkipped()) {
    plugins {
        id("io.gitlab.arturbosch.detekt")
    }

    extensions.configure<DetektExtension>("detekt") {
        buildUponDefaultConfig = true

        val globalProjectConfiguration = rootProject.layout.projectDirectory.file("config/detekt.yml").asFile
        val localProjectConfiguration = project.layout.projectDirectory.file("config/detekt.yml").asFile

        if (localProjectConfiguration.exists()) {
            config.setFrom(localProjectConfiguration, globalProjectConfiguration)
        } else {
            config.setFrom(globalProjectConfiguration)
        }

        baseline = project.layout.projectDirectory.file("config/baseline.xml").asFile
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        val detekt = extensions.getByType<DetektExtension>()
        val multiplatform = extensions.getByType<KotlinMultiplatformExtension>()

        // Source sets may be registered after this convention plugin is applied.
        detekt.source.setFrom(emptyList<Any>())
        multiplatform.sourceSets.all {
            val sources = kotlin
            detekt.source.from(providers.provider {
                val buildDir = layout.buildDirectory.get().asFile.toPath().normalize()
                sources.srcDirs.filterNot { it.toPath().normalize().startsWith(buildDir) }
            })
        }
    }

    tasks.named<Detekt>("detekt") {
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }
}

private fun Project.isDetektSkipped(): Boolean =
    findProperty("skipDetekt")?.toString()?.equals("true", ignoreCase = true) == true

