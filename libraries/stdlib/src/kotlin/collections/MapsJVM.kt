package kotlin

import java.util.Comparator
import java.util.LinkedHashMap
import java.util.Properties
import java.util.SortedMap
import java.util.TreeMap
import java.util.concurrent.ConcurrentMap

/**
 * Allows to use the index operator for storing values in a mutable map.
 */
// this code is JVM-specific, because JS has native set function
public fun <K, V> MutableMap<K, V>.set(key: K, value: V): V? = put(key, value)

/**
 * getOrPut is not supported on [ConcurrentMap] since it cannot be implemented correctly in terms of concurrency.
 * Use [concurrentGetOrPut] instead, or cast this to a [MutableMap] if you want to sacrifice the concurrent-safety.
 */
deprecated("Use concurrentGetOrPut instead or cast this map to MutableMap.")
public inline fun <K, V> ConcurrentMap<K, V>.getOrPut(key: K, defaultValue: () -> V): Nothing =
    throw UnsupportedOperationException("getOrPut is not supported on ConcurrentMap.")

/**
 * Concurrent getOrPut, that is safe for concurrent maps.
 *
 * Returns the value for the given key. If the key is not found in the map, calls the [defaultValue] function,
 * puts its result into the map under the given key and returns it.
 *
 * Note that due to concurrency [defaultValue] function may be called even if key is already in the map.
 */
public inline fun <K, V: Any> ConcurrentMap<K, V>.concurrentGetOrPut(key: K, defaultValue: () -> V): V {
    // TODO: Use computeIfAbsent on JVM8
    return this.get(key) ?:
            defaultValue().let { default -> this.putIfAbsent(key, default) ?: default }

}

/**
 * Converts this [Map] to a [SortedMap] so iteration order will be in key order.
 *
 * @sample test.collections.MapJVMTest.toSortedMap
 */
public fun <K : Comparable<K>, V> Map<K, V>.toSortedMap(): SortedMap<K, V> = TreeMap(this)

/**
 * Converts this [Map] to a [SortedMap] using the given [comparator] so that iteration order will be in the order
 * defined by the comparator.
 *
 * @sample test.collections.MapJVMTest.toSortedMapWithComparator
 */
public fun <K, V> Map<K, V>.toSortedMap(comparator: Comparator<K>): SortedMap<K, V> {
    val result = TreeMap<K, V>(comparator)
    result.putAll(this)
    return result
}

/**
 * Returns a new [SortedMap] with the specified contents, given as a list of pairs
 * where the first value is the key and the second is the value.
 *
 * @sample test.collections.MapJVMTest.createSortedMap
 */
public fun <K, V> sortedMapOf(vararg values: Pair<K, V>): SortedMap<K, V> {
    val answer = TreeMap<K, V>()
    /**
    TODO replace by this simpler call when we can pass vararg values into other methods
    answer.putAll(values)
     */
    for (v in values) {
        answer.put(v.first, v.second)
    }
    return answer
}


/**
 * Converts this [Map] to a [Properties] object.
 *
 * @sample test.collections.MapJVMTest.toProperties
 */
public fun Map<String, String>.toProperties(): Properties {
    val answer = Properties()
    for (e in this) {
        answer.put(e.key, e.value)
    }
    return answer
}

