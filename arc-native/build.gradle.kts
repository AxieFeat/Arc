plugins {
    id("io.github.arc-blroth.cargo-wrapper") version "1.2.0"
}

cargo {
    crate = projectDir.path
    outputs = mapOf("" to System.mapLibraryName("NativeEGLBridge"))
    profile = "release"
}

// Cargo tracks its own inputs and build artifacts inside the crate directory.
tasks.named("build") {
    doNotTrackState("Cargo manages incremental compilation")
}
