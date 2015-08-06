package test

enum class MyEnum(@property:deprecated("") @param:deprecated("") val ord: Int) {
    ENTRY: MyEnum(239)

    fun f(Deprecated p: Int) {

    }
}
