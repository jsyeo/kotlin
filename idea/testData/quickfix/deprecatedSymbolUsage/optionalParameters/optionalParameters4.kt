// "Replace with 'newFun(p1, p2, p3)'" "true"

interface I {
    @deprecated("", ReplaceWith("newFun(p1, p2, p3)"))
    fun oldFun(p1: String, p2: Int = p1.length(), p3: String? = p1)

    fun newFun(x: String, y: Int = x.length(), z: String? = "a")
}

fun foo(i: I) {
    i.<caret>oldFun("a")
}