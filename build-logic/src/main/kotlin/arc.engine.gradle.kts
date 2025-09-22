plugins {
    kotlin("jvm")
    `java-library`
}

kotlin {
    jvmToolchain(
        rootProject.findProperty("javaVersion").toString().toIntOrNull()
            ?: throw IllegalStateException("Java version not specified")
    )
}

applyAnnotationProcessor()
applyPublishing()
applyDetect()
