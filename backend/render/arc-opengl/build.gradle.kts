plugins {
    id("arc.publishing")
    id("arc.detekt")
    id("arc.ksp")
}

tasks.register<JavaExec>("graphicsRegression") {
    group = "verification"
    description = "Checks scoped state, preserved targets, exact stencil clipping and streaming on hidden OpenGL"
    dependsOn(tasks.testClasses)
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("arc.gl.graphics.GraphicsRegression")
}

dependencies {
    api(projects.arcCommon)
    implementation(projects.arcGlfw)

    lwjgl(libs.lwjgl.stb)
    lwjgl(libs.lwjgl.opengl)
    lwjgl(libs.lwjgl.glfw)

    implementation(libs.commonsIo)
}
