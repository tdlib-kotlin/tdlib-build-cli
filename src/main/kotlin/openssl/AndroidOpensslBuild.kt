package dev.whyoleg.tdlib.build.cli.openssl

import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import dev.whyoleg.tdlib.build.cli.*

object AndroidOpensslBuild : AbstractOpensslBuild(
    name = "android",
    help = "Build openssl for Android"
) {

    private val arch by option()
        .enum<AndroidArchitecture>()
        .help("Architecture")
        .required()

    private val api by option()
        .int()
        .help("API level")
        .default(21) //TODO default

    private val parallel by option()
        .int()
        .help("Parallelism level")
        .default(1)
        .validate { value ->
            require(value > 0) { "Parallelism level must be positive, but was $value" }
        }

    private val ndk by option()
        .file(
            mustExist = true,
            canBeFile = false,
            canBeDir = true
        )
        .help("Path to NDK")
        .required()

    override fun run() {
        val toolchain = when (Host.operatingSystem) {
            Host.OperatingSystem.Linux   -> "linux-x86_64"
            Host.OperatingSystem.Mac     -> "darwin-x86_64"
            Host.OperatingSystem.Windows -> "windows-x86_64"
            Host.OperatingSystem.Unknown -> error("Unknown operating system")
        }

        val env = mapOf(
            "ANDROID_NDK_HOME" to ndk.canonicalPath,
            "PATH" to "${ndk.canonicalPath}/toolchains/llvm/prebuilt/$toolchain/bin:${System.getenv("PATH")}",
            "CC" to "clang",
        )

        opensslDir.exec("clean", env, "make", "clean")
        opensslDir.exec("configure", env, "./Configure", "android-${arch.arch}", "-D__ANDROID_API__=$api")
        opensslDir.exec("make", env, "make", "-j$parallel")

        val destinationDir = installDir.resolve("android").resolve(arch.abi)
        val libDir = destinationDir.resolve("lib")
        opensslDir.copyFile("libcrypto.a", libDir)
        opensslDir.copyFile("libcrypto.so", libDir)
        opensslDir.copyFile("libssl.a", libDir)
        opensslDir.copyFile("libssl.so", libDir)
        opensslDir.copyDirectory("include/openssl", destinationDir)
    }

}

