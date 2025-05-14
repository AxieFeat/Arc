import pw.qubique.infrastructure.build.feature.Feature

plugins {
    checkstyle
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.0"
    id("pw.qubique.infrastructure.build-system") version ("0.0.2-snapshot")
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
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

dependencies {
    implementation(kotlin("script-runtime"))
}

fun Project.applyAnnotationProcessor() {
    if (findProperty("skipAnnotationProcessor") != "true") {
        this.dependencies {
            add("ksp", project(":arc-annotation-processor"))
        }
    }
}