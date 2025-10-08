plugins {
    id("cpp-library")
}

library {
    baseName = "MacEGLBridge"

    targetMachines = listOf(
        machines.linux.x86_64,
        machines.windows.x86,
        machines.windows.x86_64,
        machines.macOS.x86_64,
        machines.macOS.architecture("arm64")
    )

    source.from(file("src/main/cpp"))

    privateHeaders.from(file("src/main/headers"))
}

tasks.withType<CppCompile>().configureEach {
    compilerArgs.addAll(toolChain.map { toolChain ->
        val javaHome = System.getProperty("java.home")
        when {
            toolChain is Gcc || toolChain is Clang -> {
                if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
                    listOf(
                        "-x", "objective-c++",
                        "-I$javaHome/include",
                        "-I$javaHome/include/darwin"
                    )
                } else if (System.getProperty("os.name").contains("Linux", ignoreCase = true)) {
                    listOf(
                        "-I$javaHome/include",
                        "-I$javaHome/include/linux"
                    )
                } else {
                    emptyList()
                }
            }
            toolChain is VisualCpp -> listOf(
                "/I$javaHome/include",
                "/I$javaHome/include/win32"
            )
            else -> emptyList()
        }
    })
}


tasks.withType<LinkSharedLibrary>().configureEach {
    linkerArgs.addAll(toolChain.map { toolChain ->
        if (toolChain is Clang && System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
            listOf("-framework", "Cocoa", "-framework", "QuartzCore")
        } else {
            emptyList()
        }
    })
}
