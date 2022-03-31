package dev.whyoleg.tdlib.build.cli.tdlib

import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import dev.whyoleg.tdlib.build.cli.*

object LinuxTdlibBuild : AbstractTdlibBuild(
    name = "linux",
    help = "Build tdlib for Linux"
) {

    private val parallel by option()
        .int()
        .help("Parallelism level")
        .default(1)
        .validate { value ->
            require(value > 0) { "Parallelism level must be positive, but was $value" }
        }

    override fun run() {
        Host.expect(Host.OperatingSystem.Linux)

        val env = mapOf( //TODO paths to clang?
            "CXXFLAGS" to "-stdlib=libc++",
            "CC" to "clang",
            "CXX" to "clang++"
        )

        tdlibDir.resolve("build").resolve("linux").apply { clean() }.cmake {
            tdlibDir.configure("-DCMAKE_BUILD_TYPE=Release",
                "-DCMAKE_INSTALL_PREFIX:PATH=${installDir.resolve("linux").canonicalPath}",
                env = env)
//        build("prepare_cross_compiling", parallel)
            build("install", parallel)
        }
    }
}
