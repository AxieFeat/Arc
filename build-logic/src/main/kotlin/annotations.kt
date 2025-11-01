import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Apply the Arc annotation processor to the project.
 * This function adds the `arc-annotation-processor` project as a KSP dependency.
 *
 * It will be skipped, if `skipAnnotationProcessor` property is set to "true".
 */
fun Project.applyAnnotationProcessor() {
    if (findProperty("skipAnnotationProcessor") == "true") return

    plugins.apply("com.google.devtools.ksp")

    dependencies {
        add("ksp", project(":arc-annotation-processor"))
    }
}
