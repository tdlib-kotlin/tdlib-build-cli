package dev.whyoleg.tdlib.build.cli.tdlib

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*

abstract class AbstractTdlibBuild(name: String, help: String) : CliktCommand(
    name = name,
    help = help
) {
    protected val tdlibDir by option()
        .file(
            mustExist = true,
            canBeFile = false,
            canBeDir = true,
        )
        .help("Path to directory with tdlib sources")
        .required()

    protected val installDir by option()
        .file(
            mustExist = false,
            canBeFile = false,
            canBeDir = true
        )
        .help("Path to directory where to put built tdlib")
        .defaultLazy {
            tdlibDir.resolve("install")
        }
}
