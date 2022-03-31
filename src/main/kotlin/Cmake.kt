package dev.whyoleg.tdlib.build.cli

import java.io.*
import kotlin.time.*

enum class BuildType {
    MinSizeRel,
    Debug,
    Release;

    //TODO
    fun windowsCmake(): String = " --config $name"
    fun unixCmake(): String = "-DCMAKE_BUILD_TYPE=$name"
}

fun File.copyFile(path: String, destination: File): File = resolve(path).copyTo(destination.resolve(path))
fun File.copyDirectory(path: String, destination: File): Boolean =
    resolve(path).copyRecursively(destination.resolve(path))

fun File.clean() {
    deleteRecursively()
    mkdirs()
}

@OptIn(ExperimentalTime::class)
fun File.exec(tag: String, env: Map<String, String>, vararg args: String) {
    val result = measureTimedValue {
        ProcessBuilder(*args).apply {
            environment().putAll(env)
            directory(this@exec)
            redirectError(ProcessBuilder.Redirect.INHERIT)
            redirectOutput(ProcessBuilder.Redirect.INHERIT)

            println("Running `$tag` with command: ${command().joinToString(" ")}")
            println("Running directory: $canonicalPath")
        }.start().waitFor()
    }

    val message = when (result.value) {
        0    -> "Running `$tag` succeed"
        else -> "Running `$tag` failed with `${result.value}`"
    } + " in ${result.duration}"

    if (result.value != 0) error(message)
    else println(message)
}

inline fun <T> File.cmake(executable: String = "cmake", block: Cmake.() -> T): T {
    val cmake = Cmake(executable, this)
    return cmake.block()
}

class Cmake(private val executable: String, private val workingDir: File) {

    private fun exec(tag: String, vararg args: String, env: Map<String, String> = emptyMap()) {
        workingDir.exec(tag, env, executable, *args)
    }

    fun File.configure(vararg args: String, env: Map<String, String> = emptyMap()) {
        exec("configure", *args, canonicalPath, env = env)
    }

    fun build(target: String, parallel: Int) {
        exec(target, "--build", ".", "-j$parallel", "--target", target)
    }
}
