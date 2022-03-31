package dev.whyoleg.tdlib.build.cli

object Host {
    val operatingSystem: OperatingSystem
    val architecture: Architecture

    init {
        val osName = System.getProperty("os.name")
        operatingSystem = when {
            osName == "Mac OS X"     -> OperatingSystem.Mac
            osName == "Linux"        -> OperatingSystem.Linux
            osName.startsWith("Win") -> OperatingSystem.Windows
            else                     -> OperatingSystem.Unknown
        }
        architecture = when (System.getProperty("os.arch")) {
            "x86_64", "amd64" -> Architecture.X64
            "aarch64"         -> Architecture.Arm64
            else              -> Architecture.Unknown
        }
    }

    override fun toString(): String = "$operatingSystem-$architecture"

    fun expect(operatingSystem: OperatingSystem) {
        check(this.operatingSystem == operatingSystem) { "This command is for $operatingSystem only" }
    }

    fun expect(architecture: Architecture) {
        check(this.architecture == architecture) { "This command is for $architecture only" }
    }

    enum class OperatingSystem {
        Linux, Mac, Windows, Unknown
    }

    enum class Architecture {
        X64, Arm64, Unknown
    }
}
