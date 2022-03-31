package dev.whyoleg.tdlib.build.cli

//TODO: proper names
enum class AndroidArchitecture(
    val abi: String,
    val arch: String = abi,
) {
    Arm("armeabi-v7a", "arm"), //TODO: not working
    Arm64("arm64-v8a", "arm64"),
    X86("x86"),
    X64("x86_64")
}
