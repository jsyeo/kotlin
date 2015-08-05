package test

class ClassWithFunAdded {
    public fun main() {}
}

class ClassWithFunRemoved {
    public fun main() {}
    fun removed() {}
}

class ClassWithValAndFunAddedAndRemoved {
    public fun main() {}
    public val valRemoved: Int = 10
    fun funRemoved() {}
}

class ClassWithPrimaryConstructorChanged constructor() {
    public fun main() {}
}

class ClassWithSecondaryConstructorsAdded {
    public fun main() {}
}

class ClassWithSecondaryConstructorsRemoved() {
    public constructor(arg: Int): this() {}
    constructor(arg: String): this() {}
    public fun main() {}
}

class ClassWithAddedCompanionObject {
    public fun main() {}
}

class ClassWithRemovedCompanionObject {
    companion object {}
    public fun main() {}
}

class ClassWithChangedCompanionObject {
    companion object FirstName {}
    public fun main() {}
}


class ClassWithNestedClasses {
    class NestedClass1 {}
    inner class InnerClass2 {}
}

enum class EnumClassWithChanges {
    CONST_1,
    CONST_2
}