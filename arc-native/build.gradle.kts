/**
 * Google ANGLE not work with GLFW, in this reason we need to create
 * a small native bridge to create an EGL context from
 * a GLFW window. It created because LWJGL not provide such functionality out of the box.
 * This build script configures a C++ library project that compiles native code
 * to create an EGL bridge for different
 * operating systems.
 */

@file:Suppress("UnstableApiUsage")

plugins {
    id("cpp-library")
}

library {
    baseName = "NativeEGLBridge"

    targetMachines = listOf(
        machines.linux.x86_64,
        machines.windows.x86,
        machines.windows.x86_64,
        machines.macOS.x86_64,
        machines.macOS.architecture("arm64")
    )

    source.from(fileTree("src/main/cpp") {
        include(when {
            isSystem("Mac") -> "MacEGLBridge.cpp"
            else -> "StubEGLBridge.cpp" // Fallback for all other systems.
        })
    })

    privateHeaders.from(file("src/main/headers"))
}

tasks.withType<CppCompile>().configureEach {
    compilerArgs.addAll(toolChain.map { toolChain ->
        val javaHome = System.getProperty("java.home")
        when (toolChain) {
            is Gcc, is Clang -> when {
                isSystem("Mac") -> listOf(
                    "-x", "objective-c++",
                    "-I$javaHome/include",
                    "-I$javaHome/include/darwin"
                )

                else -> emptyList()
            }

            else -> emptyList()
        }
    })
}

tasks.withType<LinkSharedLibrary>().configureEach {
    linkerArgs.addAll(toolChain.map { toolChain ->
        when {
            toolChain is Clang && isSystem("Mac") ->
                listOf("-framework", "Cocoa", "-framework", "QuartzCore")

            else -> emptyList()
        }
    })
}

/**
 * This function checks if the current operating system matches the provided system name.
 *
 * @param systemName The name of the operating system to check against (e.g., "Mac", "Linux", "Windows")
 *
 * @return True if the current operating system matches the provided name, false otherwise.
 */
fun isSystem(systemName: String): Boolean =
    System.getProperty("os.name").contains(systemName, ignoreCase = true)
