package dev.whyoleg.tdlib.build.cli.tdlib

import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import dev.whyoleg.tdlib.build.cli.*

object AndroidTdlibBuild : AbstractTdlibBuild(
    name = "android",
    help = "Build tdlib for Android"
) {

    private val opensslDir by option()
        .file(
            mustExist = true,
            canBeFile = false,
            canBeDir = true,
        )
        .help("Path to directory with openssl binaries")
        .required()

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
        tdlibDir.resolve("build").resolve("android").resolve(arch.abi).apply { clean() }.cmake(
            "/home/oleg/Android/Sdk/cmake/3.22.1/bin/cmake",
        ) {
            val archOpensslDir = opensslDir.resolve(arch.abi)
            val androidArgs = arrayOf(
                "-DANDROID_NDK=${ndk.canonicalPath}",
                "-DCMAKE_TOOLCHAIN_FILE=${ndk.resolve("build/cmake/android.toolchain.cmake").canonicalPath}",
                "-DANDROID_ABI=${arch.abi}",
                "-DANDROID_NATIVE_API_LEVEL=$api",
                "-DANDROID_TOOLCHAIN=clang",
                "-DANDROID_STL=c++_static",
                "-DOPENSSL_INCLUDE_DIR=${archOpensslDir.resolve("include").canonicalPath}",
                "-DOPENSSL_CRYPTO_LIBRARY=${archOpensslDir.resolve("lib").resolve("libcrypto.a").canonicalPath}",
                "-DOPENSSL_SSL_LIBRARY=${archOpensslDir.resolve("lib").resolve("libssl.a").canonicalPath}",
            )

            tdlibDir.configure(
                "-DCMAKE_BUILD_TYPE=Release",
                "-DCMAKE_INSTALL_PREFIX:PATH=${installDir.resolve("android").resolve(arch.abi).canonicalPath}",
                *androidArgs
            )
            build("install", parallel)
//            build("install", parallel)
        }
    }
}
