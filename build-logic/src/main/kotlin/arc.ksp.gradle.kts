import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Convention plugin for modules that use the KSP annotation processor.
 *
 * Adds the `arc-annotation-processor` project as a KSP.
 *
 * It will be skipped, if `skipKsp` property is set to "true".
 */

if(!isKspSkipped()) {
    pluginManager.apply("com.google.devtools.ksp")

    dependencies {
        add("ksp", project(":arc-annotation-processor"))
    }
}

private fun Project.isKspSkipped(): Boolean =
    findProperty("skipKsp")?.toString()?.equals("true", ignoreCase = true) == true
