package a

import java.lang.Deprecated as deprecated
import java.lang.SuppressWarnings as suppresswarnings


<!DEPRECATED_JAVA_ANNOTATION!>@field:deprecated<!> @field:suppresswarnings val s: String = "";

<!DEPRECATED_JAVA_ANNOTATION!>deprecated<!> suppresswarnings fun main(args : Array<String>) {
    System.out.println("Hello, world!")
}

class Test(<!DEPRECATED_JAVA_ANNOTATION!>@field:deprecated<!> val s: String,
           @field:suppresswarnings val x : Int) {}

