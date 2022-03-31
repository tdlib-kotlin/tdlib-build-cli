package dev.whyoleg.tdlib.build.cli.tdlib

import com.github.ajalt.clikt.core.*

object RootTdlibBuild : CliktCommand(
    name = "tdlib",
    help = "Build tdlib for specific platform"
) {
    init {
        subcommands(LinuxTdlibBuild, AndroidTdlibBuild, IosTdlibBuild)
//        Windows, Mac,
//        Wasm
    }

    override fun run() {
        //TODO check
    }
}
