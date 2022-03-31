package dev.whyoleg.tdlib.build.cli.openssl

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*

abstract class AbstractOpensslBuild(name: String, help: String) : CliktCommand(
    name = name,
    help = help
) {
    protected val opensslDir by option()
        .file(
            mustExist = true,
            canBeFile = false,
            canBeDir = true,
        )
        .help("Path to directory with openssl sources")
        .required()

    protected val installDir by option()
        .file(
            mustExist = false,
            canBeFile = false,
            canBeDir = true
        )
        .help("Path to directory where to put built openssl")
        .defaultLazy {
            opensslDir.resolve("install")
        }
}
