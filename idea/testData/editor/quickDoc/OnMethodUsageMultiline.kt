/**
 * Some documentation
 * on two lines.
 */
fun testMethod() {

}

fun test() {
    <caret>testMethod()
}

//INFO: <b>internal</b> <b>fun</b> testMethod(): Unit <i>defined in</i> root package<p>Some documentation on two lines.</p>
