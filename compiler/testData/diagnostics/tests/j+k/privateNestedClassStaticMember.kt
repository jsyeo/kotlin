// KT-4149 static members of Java private nested class are accessible from Kotlin

// FILE: javaPackage/Foo.java

package javaPackage;

public class Foo {
    private static class Bar {
        public static void doSmth() {
        }
    }
}

// FILE: A.kt

fun main(args: Array<String>) {
    javaPackage.Foo.Bar.<!INVISIBLE_MEMBER!>doSmth<!>()
}
