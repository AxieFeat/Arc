import pw.qubique.infrastructure.build.feature.Feature

plugins {
    alias(libs.plugins.checkstyle)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildSystem)
}

subprojects {
    apply(plugin = "checkstyle")
    apply(plugin = "pw.qubique.infrastructure.build-system")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.google.devtools.ksp")

    group = "arc.engine"
    version = "1.0"

    applyAnnotationProcessor()

    checkstyle {
        configFile = file("${rootDir}/infrastructure/checkstyle/checkstyle.xml")
    }

    build {
        features.set(
            listOf(
                Feature.SCM,
                Feature.TEST,
                Feature.CHECKSTYLE,
                Feature.JACOCO,
                Feature.JAVADOC,
                Feature.REPOSITORIES,
                Feature.PUBLISHING,
            )
        )
    }

    repositories {
        mavenCentral()
    }

    kotlin {
        jvmToolchain(21)
    }
}

fun Project.applyAnnotationProcessor() {
    if (findProperty("skipAnnotationProcessor") == "true") return

    dependencies {
        add("ksp", project(":arc-annotation-processor"))
    }
}