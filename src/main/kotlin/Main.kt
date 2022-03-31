package dev.whyoleg.tdlib.build.cli

import com.github.ajalt.clikt.core.*

fun main(args: Array<String>): Unit = Main.main(testArgs)

private val testArgs: List<String> = listOf(
    "build",

//    "openssl",
//    "android",
//    "--openssl-dir", "../openssl",
//    "--install-dir", "../built/openssl",
//    "--arch", "arm64",
//    "--ndk", "/home/oleg/Android/Sdk/ndk/24.0.8215888",

    "tdlib",
    "android",
    "--tdlib-dir", "../tdlib",
    "--install-dir", "../built/tdlib",
    "--arch", "arm64",
    "--ndk", "/home/oleg/Android/Sdk/ndk/24.0.8215888",
    "--openssl-dir", "../built/openssl/android",

//    "linux",
//    "--tdlib-dir", "../tdlib",
//    "--build-dir", "../tdlib/build/linux", //optional
//    "--install-dir", "../built/tdlib",
//    "--install-dir", "../tdlib/install/linux", //optional
//    "--clean",
    "--parallel=6"
//    "--help",
)

object Main : CliktCommand(name = "tdlib-build-cli") {
    init {
        subcommands(RootBuild)
    }

    override fun run() {
        Host.expect(Host.Architecture.X64)
    }
}
