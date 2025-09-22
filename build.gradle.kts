plugins {
    id("arc.engine")
}

allprojects {
    apply(plugin = "arc.engine")

    group = rootProject.findProperty("group") ?: throw IllegalStateException("Project group not specified")
    version = rootProject.findProperty("version") ?: throw IllegalStateException("Project version not specified")
    description = rootProject.findProperty("description") as? String ?: run {
        logger.warn("Project description not found, fallback to empty string.")
        return@run ""
    }
}
