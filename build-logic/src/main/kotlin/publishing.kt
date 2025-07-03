import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get
import java.io.File
import java.util.concurrent.TimeUnit

val gitCommitHash = "git rev-parse --short=7 HEAD".runCommand()

fun Project.applyPublishing() {
    plugins.withId("maven-publish") {
        extensions.configure(PublishingExtension::class.java) {
            publications.create("maven", MavenPublication::class.java) {
                groupId = rootProject.group.toString()
                artifactId = project.name
                version = gitCommitHash

                from(components["kotlin"])
            }

            repositories.maven {
                this.name = "GitHubPackages"
                this.url = uri("https://github.com/AxieFeat/Arc/")
                this.credentials {
                    this.username = System.getenv("GITHUB_ACTOR")
                    this.password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

private fun String.runCommand(workingDir: File = File("./")): String {
    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    proc.waitFor(1, TimeUnit.MINUTES)
    return proc.inputStream.bufferedReader().readText().trim()
}