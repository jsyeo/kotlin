/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.utils

import java.util.AbstractSet
import java.util.Arrays
import java.util.LinkedHashSet
import java.util.NoSuchElementException

/**
 * A set which maintains the order in which the elements were added and is optimized for small sizes.
 * This set is not synchronized and it does not support removal operations: [remove], [removeAll], [retainAll].
 * Also, [iterator] returns an iterator which does not support [MutableIterator.remove].
 */
@suppress("UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")
public class SmartSet<T> : AbstractSet<T>() {
    private companion object {
        val ARRAY_THRESHOLD = 5
    }

    // null if size = 0, object if size = 1, array of objects if size < threshold, linked hash set otherwise
    private var obj: Any? = null

    private var size: Int = 0

    override fun iterator(): MutableIterator<T> = when {
        size == 0 -> EmptyIterator as MutableIterator<T>
        size == 1 -> SingletonIterator(obj as T)
        size < ARRAY_THRESHOLD -> (obj as Array<T>).iterator() as MutableIterator
        else -> (obj as MutableSet<T>).iterator()
    }

    override fun add(e: T): Boolean {
        when {
            size == 0 -> {
                obj = e
            }
            size == 1 -> {
                if (obj == e) return false
                obj = arrayOf(obj, e)
            }
            size < ARRAY_THRESHOLD -> {
                val arr = obj as Array<T>
                for (element in arr) {
                    if (element == e) return false
                }
                obj = if (size == ARRAY_THRESHOLD - 1) LinkedHashSet(arr.asList()).apply { add(e) }
                else Arrays.copyOf(arr, size + 1).apply { set(size() - 1, e) }
            }
            else -> {
                val set = obj as MutableSet<T>
                if (!set.add(e)) return false
            }
        }

        size++
        return true
    }

    override fun clear() {
        obj = null
        size = 0
    }

    override fun size(): Int = size

    override fun contains(o: Any?): Boolean = when {
        size == 0 -> false
        size == 1 -> obj == o
        size < ARRAY_THRESHOLD -> o in obj as Array<T>
        else -> o in obj as Set<T>
    }
}

internal object EmptyIterator : MutableIterator<Nothing?> {
    override fun next(): Nothing? = throw NoSuchElementException()

    override fun hasNext(): Boolean = false

    override fun remove(): Unit = throw IllegalStateException("The collection is empty.")
}

internal class SingletonIterator<T>(private val element: T) : MutableIterator<T> {
    private var finished = false

    override fun next(): T =
            if (finished) throw NoSuchElementException()
            else {
                finished = true
                element
            }

    override fun hasNext(): Boolean = !finished

    override fun remove(): Unit = throw UnsupportedOperationException("This iterator does not support 'remove()'.")
}
