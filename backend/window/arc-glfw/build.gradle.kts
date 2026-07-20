plugins {
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

dependencies {
    api(projects.arcCommon)

    lwjgl(libs.lwjgl.glfw)
}
