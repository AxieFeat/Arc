import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import java.io.File
import java.util.concurrent.TimeUnit

private val gitCommitHash = "git rev-parse --short=7 HEAD".runCommand()

/**
 * Apply publishing configuration to the project.
 * This function sets up a Maven publication with the project's main component and a sources JAR.
 * It also configures the repository to publish to GitHub Packages using environment variables for authentication.
 */
fun Project.applyPublishing() {
    plugins.withId("maven-publish") {
        val sourcesJar = tasks.register("sourcesJar", Jar::class) {
            archiveClassifier.set("sources")
            from(project.the<SourceSetContainer>().named<SourceSet>("main").get().allSource)
        }

        extensions.configure(PublishingExtension::class.java) {
            publications.create("maven", MavenPublication::class.java) {
                groupId = rootProject.group.toString()
                artifactId = project.name
                version = gitCommitHash

                from(components["kotlin"])

                artifact(sourcesJar)
            }

            repositories.maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/AxieFeat/Arc")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

/**
 * Generally this function runs a command in the terminal and returns the output as a string.
 * It takes an optional working directory parameter, defaulting to the current directory.
 *
 * But in fact this function used only for getting git commit hash for versioning in publishing.
 *
 * @param workingDir The directory in which to run the command. Defaults to the current directory.
 *
 * @return The output of the command as a trimmed string.
 */
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