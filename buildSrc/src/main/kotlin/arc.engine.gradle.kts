plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    `java-library`
    `maven-publish`
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        // TODO
        create<MavenPublication>("maven") {
            groupId = "arc.engine"
            artifactId = project.name
            version = "1.0"

            from(components["kotlin"])
        }
    }
}

applyAnnotationProcessor()

fun Project.applyAnnotationProcessor() {
    if (findProperty("skipAnnotationProcessor") == "true") return

    dependencies {
        add("ksp", project(":arc-annotation-processor"))
    }
}