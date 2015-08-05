package test

class ClassWithFunAdded {
    public fun main() {}
    fun added() {}
}

class ClassWithFunRemoved {
    public fun main() {}
}

class ClassWithValAndFunAddedAndRemoved {
    public fun main() {}
    public val valAdded: String = ""
    fun funAdded() {}
}

class ClassWithPrimaryConstructorChanged constructor(arg: String) {
    public fun main() {}
}

class ClassWithSecondaryConstructorsAdded() {
    constructor(arg: Int): this() {}
    constructor(arg: String): this() {}
    public fun main() {}
}

class ClassWithSecondaryConstructorsRemoved() {
    public fun main() {}
}

class ClassWithAddedCompanionObject {
    companion object {}
    public fun main() {}
}

class ClassWithRemovedCompanionObject {
    public fun main() {}
}

class ClassWithChangedCompanionObject {
    companion object SecondName {}
    public fun main() {}
}

class ClassWithNestedClasses {
    class NestedClass3 {}
    inner class InnerClass2 {}
    inner class InnerClass4 {}
}

enum class EnumClassWithChanges {
    CONST_1,
    CONST_3,
    CONST_4
}