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

package org.jetbrains.kotlin.util

import org.jetbrains.kotlin.descriptors.annotations.AnnotationUseSiteTarget
import org.jetbrains.kotlin.descriptors.annotations.AnnotationWithTarget
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.name.FqName

public class TargetFilteredAnnotations(
        private val delegate: Annotations,
        private val excludedTarget: AnnotationUseSiteTarget
) : Annotations {

    override fun isEmpty() = delegate.isEmpty()

    override fun findAnnotation(fqName: FqName) = delegate.findAnnotation(fqName)

    override fun findExternalAnnotation(fqName: FqName) = delegate.findExternalAnnotation(fqName)

    override fun getUseSiteTargetedAnnotations() = getTargetedAnnotations()

    override fun getAllAnnotations() = delegate.map { AnnotationWithTarget(it, null) } + getTargetedAnnotations()

    override fun iterator() = delegate.iterator()

    private fun getTargetedAnnotations() = delegate.getUseSiteTargetedAnnotations().filter { excludedTarget != it.target }

}