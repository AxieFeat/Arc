import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Convention plugin for publishing library modules to GitHub Packages.
 *
 * The GitHub Actions workflow provides GITHUB_SHA, GITHUB_ACTOR and GITHUB_TOKEN.
 * For local publishing, -PpublishingVersion, -Pgpr.user and -Pgpr.key can be used.
 *
 * It will be skipped, if `skipPublishing` property is set to "true".
 */
if (!isPublishingSkipped()) {
    pluginManager.apply("arc.base")
    pluginManager.apply("maven-publish")

    val publishingVersion = providers.gradleProperty("publishingVersion")
        .orElse(providers.environmentVariable("GITHUB_SHA").map { it.take(7) })
        .orElse(providers.provider { shortGitCommitHash(rootProject.layout.projectDirectory.asFile) ?: version.toString() })

    val githubRepository = providers.gradleProperty("githubRepository")
        .orElse(providers.environmentVariable("GITHUB_REPOSITORY"))
        .orElse("AxieFeat/${rootProject.name}")

    val githubActor = providers.gradleProperty("gpr.user")
        .orElse(providers.environmentVariable("GITHUB_ACTOR"))
        .orElse(providers.environmentVariable("GITHUB_REPOSITORY_OWNER"))

    val githubToken = providers.gradleProperty("gpr.key")
        .orElse(providers.environmentVariable("GITHUB_TOKEN"))

    extensions.configure<JavaPluginExtension> {
        withSourcesJar()
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = project.name
                version = publishingVersion.get()

                from(project.components.getByName("java"))
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/${githubRepository.get()}")
                credentials {
                    username = githubActor.orNull
                    password = githubToken.orNull
                }
            }
        }
    }
}

private fun Project.isPublishingSkipped(): Boolean =
    findProperty("skipPublishing")?.toString()?.equals("true", ignoreCase = true) == true

private fun shortGitCommitHash(workingDir: File): String? {
    return try {
        val process = ProcessBuilder("git", "rev-parse", "--short=7", "HEAD")
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        if (!process.waitFor(10, TimeUnit.SECONDS)) {
            process.destroyForcibly()
            return null
        }

        if (process.exitValue() != 0) {
            return null
        }

        process.inputStream.bufferedReader().readText().trim().takeIf { it.isNotEmpty() }
    } catch (_: IOException) {
        null
    } catch (_: SecurityException) {
        null
    } catch (_: InterruptedException) {
        Thread.currentThread().interrupt()
        null
    }
}
