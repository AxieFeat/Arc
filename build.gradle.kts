import pw.qubique.infrastructure.build.feature.Feature

plugins {
    checkstyle
    kotlin("jvm") version "2.0.21"
    id("pw.qubique.infrastructure.build-system") version ("0.0.2-snapshot")
    kotlin("plugin.serialization") version "2.0.0"
}

allprojects {
    apply(plugin = "checkstyle")
    apply(plugin = "pw.qubique.infrastructure.build-system")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "arc.engine"
    version = "1.0"

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