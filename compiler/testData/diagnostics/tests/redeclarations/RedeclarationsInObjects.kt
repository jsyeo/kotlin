// KT-3525
<!CONFLICTING_JVM_DECLARATIONS!>object B<!> {
    class <!REDECLARATION!>C<!>
    class <!REDECLARATION!>C<!>

    val <!REDECLARATION!>a<!> : Int = 1
    val <!REDECLARATION!>a<!> : Int = 1
}