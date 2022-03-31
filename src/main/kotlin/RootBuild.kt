package dev.whyoleg.tdlib.build.cli

import com.github.ajalt.clikt.core.*
import dev.whyoleg.tdlib.build.cli.openssl.*
import dev.whyoleg.tdlib.build.cli.tdlib.*

object RootBuild : CliktCommand(name = "build") {
    init {
        subcommands(RootTdlibBuild, RootOpensslBuild)
    }

    override fun run() {}
}
