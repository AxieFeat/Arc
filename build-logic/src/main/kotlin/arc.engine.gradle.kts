plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    `java-library`
    `maven-publish`
}

kotlin {
    jvmToolchain(
        rootProject.findProperty("javaVersion").toString().toIntOrNull()
            ?: throw IllegalStateException("Java version not specified")
    )
}

applyAnnotationProcessor()
applyPublishing()