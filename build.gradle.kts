plugins {
    kotlin("jvm") version "2.0.21"
    id("java-library")
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")

    group = "arc.engine"
    version = "1.0"

    repositories {
        mavenCentral()
        mavenLocal()
    }

    kotlin {
        jvmToolchain(21)
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    }

    tasks.test {
        useJUnitPlatform()
    }

}

dependencies {
    implementation(kotlin("script-runtime"))
}