// TODO Refactor?
subprojects {
    apply(plugin = "arc.publishing")
//    apply(plugin = "arc.detekt") // TODO Enable when got stable
    apply(plugin = "arc.ksp")


    dependencies {
        api(project(":arc-core"))
    }
}
