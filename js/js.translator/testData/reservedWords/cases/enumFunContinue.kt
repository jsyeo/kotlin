package foo

// NOTE THIS FILE IS AUTO-GENERATED by the generateTestDataForReservedWords.kt. DO NOT EDIT!

enum class Foo {
    BAR;
    fun `continue`() { `continue`() }

    fun test() {
        testNotRenamed("continue", { ::`continue` })
    }
}

fun box(): String {
    Foo.BAR.test()

    return "OK"
}