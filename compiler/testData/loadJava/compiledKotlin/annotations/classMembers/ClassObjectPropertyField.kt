package test

annotation class Anno

class Class {
    companion object {
        @field:Anno var property: Int = 42
        @Anno var property2: Int = 43
    }
}
