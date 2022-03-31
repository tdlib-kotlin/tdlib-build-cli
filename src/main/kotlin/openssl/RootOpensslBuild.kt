package dev.whyoleg.tdlib.build.cli.openssl

import com.github.ajalt.clikt.core.*

object RootOpensslBuild : CliktCommand(
    name = "openssl",
    help = "Build openssl for specific platform"
) {
    init {
        subcommands(AndroidOpensslBuild, IosOpensslBuild)
    }

    override fun run() {
        //TODO check
    }
}
