fun box(): String {
    val o = "OK" charAt 0
    val array = CharArray(2)
    array[1] = 'K'
    val k = array get 1

    return "$o$k"
}