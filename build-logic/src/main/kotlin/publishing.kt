import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get

fun Project.applyPublishing() {
    plugins.withId("maven-publish") {
        extensions.configure(PublishingExtension::class.java) {
            publications.create("maven", MavenPublication::class.java) {
                groupId = rootProject.group.toString()
                artifactId = project.name
                version = rootProject.version.toString()

                from(components["kotlin"])
            }
        }
    }
}