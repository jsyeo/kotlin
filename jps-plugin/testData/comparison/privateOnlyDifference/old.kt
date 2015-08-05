package test

class ClassWithPrivateFunAdded {
    public fun main() {}
    val s = java.lang.String.valueOf(20)
}

class ClassWithPrivateFunRemoved {
    public fun main() {}
    private fun privateFun() {}
}

class ClassWithPrivateFunSignatureChanged {
    public fun main() {}
    private fun privateFun(arg: String) {}
}

class ClassWithPrivateValAdded {
    public fun main() {}
}

class ClassWithPrivateValRemoved {
    public fun main() {}
    private val x: Int = 100
}

class ClassWithPrivateValSignatureChanged {
    public fun main() {}
    private val x: Int = 100
}

class ClassWithPrivateVarAdded {
    public fun main() {}
}

class ClassWithPrivateVarRemoved {
    public fun main() {}
    private var x: Int = 100
}

class ClassWithPrivateVarSignatureChanged {
    public fun main() {}
    private var x: Int = 100
}

class ClassWithGetterForPrivateValChanged {
    public fun main() {}
    private val x: Int = 100
}

class ClassWithGetterAndSetterForPrivateVarChanged {
    public fun main() {}
    private var x: Int = 100
}

class ClassWithPrivatePrimaryConstructorAdded {
    public fun main() {}
    private constructor(arg: Int) {}
}

class ClassWithPrivatePrimaryConstructorRemoved private constructor() {
    public fun main() {}
    private constructor(arg: Int) : this() {}
}

class ClassWithPrivatePrimaryConstructorChanged private constructor() {
    public fun main() {}
}

class ClassWithPrivateSecondaryConstructorsAdded {
    public fun main() {}
}

class ClassWithPrivateSecondaryConstructorsAdded2() {
    public fun main() {}
    constructor(arg: Float) : this() {}
}

class ClassWithPrivateSecondaryConstructorsRemoved() {
    private constructor(arg: Int): this() {}
    private constructor(arg: String): this() {}
    public fun main() {}
}





