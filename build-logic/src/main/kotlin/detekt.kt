import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

fun Project.applyDetect() {
    if (findProperty("skipDetect") == "true") return

    plugins.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension>("detekt") {
        buildUponDefaultConfig = true

        val globalProjectConfiguration = file("${rootProject.rootDir}/config/detekt.yml")
        val localProjectConfiguration = file("config/detekt.yml")

        config = if(localProjectConfiguration.exists()) {
            files(localProjectConfiguration, globalProjectConfiguration)
        } else files(globalProjectConfiguration)

        baseline = file("config/baseline.xml")
    }
    tasks.named<Detekt>("detekt") {
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }
}
