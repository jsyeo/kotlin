package test

class ClassWithPrivateFunAdded {
    public fun main() {}
    private fun privateFun() {}
    val s = java.lang.String.valueOf(20)
}

class ClassWithPrivateFunRemoved {
    public fun main() {}
}

class ClassWithPrivateFunSignatureChanged {
    public fun main() {}
    private fun privateFun(arg: Int) {}
}

class ClassWithPrivateValAdded {
    public fun main() {}
    private val x: Int = 100
}

class ClassWithPrivateValRemoved {
    public fun main() {}
}

class ClassWithPrivateValSignatureChanged {
    public fun main() {}
    private val x: String = "X"
}

class ClassWithPrivateVarAdded {
    public fun main() {}
    private var x: Int = 100
}

class ClassWithPrivateVarRemoved {
    public fun main() {}
}

class ClassWithPrivateVarSignatureChanged {
    public fun main() {}
    private var x: String = "X"
}

class ClassWithGetterForPrivateValChanged {
    public fun main() {}
    private val x: Int
    get() = 200
}

class ClassWithGetterAndSetterForPrivateVarChanged {
    public fun main() {}
    private var x: Int
    get() = 200
    set(value) {}
}

class ClassWithPrivatePrimaryConstructorAdded private constructor() {
    public fun main() {}
    private constructor(arg: Int) : this() {}
}

class ClassWithPrivatePrimaryConstructorRemoved {
    public fun main() {}
    private constructor(arg: Int) {}
}

class ClassWithPrivatePrimaryConstructorChanged private constructor(arg: String) {
    public fun main() {}
}

class ClassWithPrivateSecondaryConstructorsAdded() {
    private constructor(arg: Int) : this() {}
    private constructor(arg: String) : this() {}
    public fun main() {}
}

class ClassWithPrivateSecondaryConstructorsAdded2() {
    public fun main() {}
    private constructor(arg: Int) : this() {}
    private constructor(arg: String) : this() {}
    constructor(arg: Float) : this() {}
}

class ClassWithPrivateSecondaryConstructorsRemoved() {
    public fun main() {}
}
