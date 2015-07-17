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

package org.jetbrains.kotlin.descriptors.annotations

import java.util.*
import kotlin.annotation
import kotlin.reflect.jvm.internal.impl.descriptors.annotations

// NOTE: this enum must have the same entries with kotlin.annotation.AnnotationTarget
public enum class AnnotationTarget(val description: String, val isDefault: Boolean = true) {
    PACKAGE("package"),
    CLASSIFIER("classifier"),
    ANNOTATION_CLASS("annotation class"),
    TYPE_PARAMETER("type parameter", false),
    PROPERTY("property"),
    FIELD("field"),
    LOCAL_VARIABLE("local variable"),
    VALUE_PARAMETER("value parameter"),
    CONSTRUCTOR("constructor"),
    FUNCTION("function"),
    PROPERTY_GETTER("getter"),
    PROPERTY_SETTER("setter"),
    TYPE("type usage", false),
    EXPRESSION("expression", false),
    FILE("file", false);

    companion object {

        private val map = HashMap<String, AnnotationTarget>()

        init {
            for (target in AnnotationTarget.values()) {
                map[target.name()] = target
            }
        }

        public fun valueOrNull(name: String): AnnotationTarget? = map[name]

        public val DEFAULT_TARGET_SET: Set<AnnotationTarget> = values().filter { it.isDefault }.toSet()

        public val ALL_TARGET_SET: Set<AnnotationTarget> = values().toSet()

        public fun mapUseSiteTarget(target: AnnotationUseSiteTarget): List<AnnotationTarget> = USE_SITE_MAPPING[target] ?: listOf()

        private val USE_SITE_MAPPING: Map<AnnotationUseSiteTarget, List<AnnotationTarget>> = mapOf(
                AnnotationUseSiteTarget.CONSTRUCTOR_PARAMETER to listOf(VALUE_PARAMETER, PROPERTY),
                AnnotationUseSiteTarget.FIELD to listOf(FIELD),
                AnnotationUseSiteTarget.PROPERTY to listOf(PROPERTY),
                AnnotationUseSiteTarget.FILE to listOf(FILE),
                AnnotationUseSiteTarget.PROPERTY_GETTER to listOf(PROPERTY_GETTER),
                AnnotationUseSiteTarget.PROPERTY_SETTER to listOf(PROPERTY_SETTER),
                AnnotationUseSiteTarget.RECEIVER to listOf(VALUE_PARAMETER),
                AnnotationUseSiteTarget.SETTER_PARAMETER to listOf(VALUE_PARAMETER))

    }
}