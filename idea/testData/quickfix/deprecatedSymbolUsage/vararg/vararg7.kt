// "Replace with 'newFun(p1, *p2)'" "true"

@deprecated("", ReplaceWith("newFun(p1, *p2)"))
fun oldFun(p1: String, p2: IntArray) {
    newFun(p1, *p2)
}

fun newFun(p1: String, vararg p2: Int){}

fun foo(array: IntArray) {
    <caret>oldFun("a", array)
}