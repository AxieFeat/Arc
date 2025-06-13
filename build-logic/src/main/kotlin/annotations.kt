import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

fun Project.applyAnnotationProcessor() {
    if (findProperty("skipAnnotationProcessor") == "true") return

    dependencies {
        add("ksp", project(":arc-annotation-processor"))
    }
}