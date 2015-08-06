val <!REDECLARATION, REDECLARATION!>a<!> : Int = 1
val <!REDECLARATION, REDECLARATION!>a<!> : Int = 1
val <!REDECLARATION, REDECLARATION!>a<!> : Int = 1

val <!REDECLARATION, REDECLARATION!>b<!> : Int = 1
val <!REDECLARATION, REDECLARATION!>b<!> : Int = 1
val <!REDECLARATION, REDECLARATION!>b<!> : Int = 1
val <!REDECLARATION, REDECLARATION!>b<!> : Int = 1

<!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here too
<!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here
<!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here
<!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here

<!CONFLICTING_OVERLOADS!>fun bar()<!> {} // and here
<!CONFLICTING_OVERLOADS!>fun bar()<!> {} // and here
<!CONFLICTING_OVERLOADS!>fun bar()<!> {} // and here

class <!CONFLICTING_JVM_DECLARATIONS, CONFLICTING_JVM_DECLARATIONS!>A<!> {
    val <!REDECLARATION!>a<!> : Int = 1
    val <!REDECLARATION!>a<!> : Int = 1
    val <!REDECLARATION!>a<!> : Int = 1

    val <!REDECLARATION!>b<!> : Int = 1
    val <!REDECLARATION!>b<!> : Int = 1
    val <!REDECLARATION!>b<!> : Int = 1
    val <!REDECLARATION!>b<!> : Int = 1

    <!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here too
    <!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here
    <!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here
    <!CONFLICTING_OVERLOADS!>fun foo()<!> {} // and here

    <!CONFLICTING_OVERLOADS!>fun bar()<!> {} // and here
    <!CONFLICTING_OVERLOADS!>fun bar()<!> {} // and here
    <!CONFLICTING_OVERLOADS!>fun bar()<!> {} // and here
}