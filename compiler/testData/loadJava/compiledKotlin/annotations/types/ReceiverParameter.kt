package test

target(AnnotationTarget.TYPE)
annotation class A

fun @A String.foo() {}
