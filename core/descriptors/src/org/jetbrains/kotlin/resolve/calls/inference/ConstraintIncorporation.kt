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

package org.jetbrains.kotlin.resolve.calls.inference

import org.jetbrains.kotlin.descriptors.TypeParameterDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.resolve.calls.inference.ConstraintSystemImpl.ConstraintKind.EQUAL
import org.jetbrains.kotlin.resolve.calls.inference.ConstraintSystemImpl.ConstraintKind.SUB_TYPE
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.Bound
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.BoundKind
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.BoundKind.EXACT_BOUND
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.BoundKind.LOWER_BOUND
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.BoundKind.UPPER_BOUND
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.CompoundConstraintPosition
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.ConstraintPositionKind
import org.jetbrains.kotlin.resolve.scopes.JetScope
import org.jetbrains.kotlin.types.*
import org.jetbrains.kotlin.types.Variance.INVARIANT
import org.jetbrains.kotlin.types.Variance.IN_VARIANCE
import org.jetbrains.kotlin.types.typeUtil.getNestedTypeArguments
import java.util.ArrayList

fun ConstraintSystemImpl.incorporateConstraint(variable: JetType, constrainingBound: Bound) {
    val typeBounds = getTypeBounds(variable)
    val typeVariable = getMyTypeVariable(variable)!!

    val bounds = ArrayList(typeBounds.bounds)
    for (variableBound in bounds) {
        addConstraintFromBounds(variableBound, constrainingBound)
    }
    val dependentBounds = ArrayList(getDependentBounds(typeVariable))
    for (dependentBound in dependentBounds) {
        val type = JetTypeImpl(Annotations.EMPTY, dependentBound.typeVariable.getTypeConstructor(), false, listOf(), JetScope.Empty)
        generateNewConstraint(type, dependentBound, constrainingBound)
    }

    val constrainingType = constrainingBound.constrainingType
    if (isMyTypeVariable(constrainingType)) {
        val bound = Bound(variable, constrainingBound.kind.reverse(), constrainingBound.position, pure = false)
        addBound(constrainingType, bound)
        return
    }
    constrainingType.getNestedTypeArguments().forEach {
        val argument = it.getType()
        if (isMyTypeVariable(argument)) {
            for (variableBound in getTypeBounds(argument).bounds) {
                generateNewConstraint(variable, constrainingBound, variableBound)
            }
        }
    }
}

private fun ConstraintSystemImpl.addConstraintFromBounds(first: Bound, second: Bound) {
    if (first == second) return

    val firstType = first.constrainingType
    val secondType = second.constrainingType
    val position = CompoundConstraintPosition(first.position, second.position)

    when (first.kind to second.kind) {
        LOWER_BOUND to UPPER_BOUND, LOWER_BOUND to EXACT_BOUND, EXACT_BOUND to UPPER_BOUND ->
            addConstraint(SUB_TYPE, firstType, secondType, position)

        UPPER_BOUND to LOWER_BOUND, UPPER_BOUND to EXACT_BOUND, EXACT_BOUND to LOWER_BOUND ->
            addConstraint(SUB_TYPE, secondType, firstType, position)

        EXACT_BOUND to EXACT_BOUND ->
            addConstraint(EQUAL, firstType, secondType, position)
    }
}

private fun ConstraintSystemImpl.generateNewConstraint(
        variable: JetType,
        bound: Bound,
        substitution: Bound
) {
    // Let's have a variable T, a bound 'T <=> My<R>', and a substitution 'R <=> Type'.
    // Here <=> means lower_bound, upper_bound or exact_bound constraint.
    // Then a new bound 'T <=> My<Type>' can be generated.

    // A variance of R in 'My<R>' (with respect to both use-site and declaration-site variance).
    val substitutionVariance: Variance = bound.constrainingType.getNestedTypeArguments().firstOrNull {
        getMyTypeVariable(it.getType()) === substitution.typeVariable
    }?.getProjectionKind() ?: return

    val newKind = computeKindOfNewBound(bound.kind, substitutionVariance, substitution.kind)
    // We don't substitute anything into recursive constraints
    if (newKind == null || substitution.typeVariable == bound.typeVariable) return

    val newTypeProjection = TypeProjectionImpl(substitutionVariance, substitution.constrainingType)
    val substitutor = TypeSubstitutor.create(mapOf(substitution.typeVariable.getTypeConstructor() to newTypeProjection))
    val newConstrainingType = substitutor.substitute(bound.constrainingType, INVARIANT)!!

    // We don't generate new recursive constraints
    if (newConstrainingType.getNestedTypeVariables().contains(bound.typeVariable)) return

    val pure = with (this) { newConstrainingType.isPure() }
    val position = CompoundConstraintPosition(bound.position, substitution.position)
    addBound(variable, Bound(newConstrainingType, newKind, position, pure))
}

private fun computeKindOfNewBound(constrainingKind: BoundKind, substitutionVariance: Variance, substitutionKind: BoundKind): BoundKind? {
    // In examples below: List<out T>, MutableList<T>, Comparator<in T>, the variance of My<T> may be any.

    // T <=> My<R>, R <=> Type -> T <=> My<Type>

    // T < My<R>, R = Int -> T < My<Int>
    if (substitutionKind == EXACT_BOUND) return constrainingKind

    // T < MutableList<R>, R < Number - nothing can be inferred (R might become 'Int' later)
    if (substitutionVariance == INVARIANT) return null

    val kind = if (substitutionVariance == IN_VARIANCE) substitutionKind.reverse() else substitutionKind

    // T = List<R>, R < Int -> T < List<Int>; T = Consumer<R>, R < Int -> T > Consumer<Int>
    if (constrainingKind == EXACT_BOUND) return kind

    // T < List<R>, R < Int -> T < List<Int>; T < Consumer<R>, R > Int -> T < Consumer<Int>
    if (constrainingKind == kind) return kind

    // otherwise we can generate no new constraints
    return null
}

fun ConstraintSystemImpl.fixVariable(typeVariable: TypeParameterDescriptor) {
    val typeBounds = getTypeBounds(typeVariable)
    if (typeBounds.isFixed) return
    typeBounds.setFixed()

    val nestedTypeVariables = typeBounds.bounds.flatMap { it.constrainingType.getNestedTypeVariables() }
    nestedTypeVariables.forEach { fixVariable(it) }

    val value = typeBounds.value ?: return

    val type = JetTypeImpl(Annotations.EMPTY, typeVariable.getTypeConstructor(), false, emptyList(), JetScope.Empty)
    addBound(type, TypeBounds.Bound(value, BoundKind.EXACT_BOUND, ConstraintPositionKind.FROM_COMPLETER.position()))
}

fun ConstraintSystemImpl.fixVariablesInType(type: JetType) {
    val nestedTypeVariables = type.getNestedTypeVariables()
    nestedTypeVariables.forEach { fixVariable(it) }
}

fun ConstraintSystemImpl.fixVariables() {
    val (local, nonlocal) = getTypeVariables().partition { isLocalVariable(it) }
    nonlocal.forEach { fixVariable(it) }
    local.forEach { fixVariable(it) }
}