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

package org.jetbrains.kotlin.idea.quickfix.createFromUsage.createCallable

import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.diagnostics.Diagnostic
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.idea.quickfix.createFromUsage.callableBuilder.CallableInfo
import org.jetbrains.kotlin.idea.quickfix.createFromUsage.callableBuilder.FunctionInfo
import org.jetbrains.kotlin.idea.quickfix.createFromUsage.callableBuilder.ParameterInfo
import org.jetbrains.kotlin.idea.quickfix.createFromUsage.callableBuilder.TypeInfo
import org.jetbrains.kotlin.psi.JetCallExpression
import org.jetbrains.kotlin.types.Variance
import org.jetbrains.kotlin.types.expressions.OperatorConventions

object CreateInvokeFunctionActionFactory : CreateCallableMemberFromUsageFactory<JetCallExpression>() {
    override fun getElementOfInterest(diagnostic: Diagnostic): JetCallExpression? {
        return diagnostic.psiElement.parent as? JetCallExpression
    }

    override fun createCallableInfo(element: JetCallExpression, diagnostic: Diagnostic): CallableInfo? {
        val expectedType = Errors.FUNCTION_EXPECTED.cast(diagnostic).b
        if (expectedType.isError) return null

        val receiverType = TypeInfo(expectedType, Variance.IN_VARIANCE)

        val anyType = KotlinBuiltIns.getInstance().nullableAnyType
        val parameters = element.valueArguments.map {
            ParameterInfo(
                    it.getArgumentExpression()?.let { TypeInfo(it, Variance.IN_VARIANCE) } ?: TypeInfo(anyType, Variance.IN_VARIANCE),
                    it.getArgumentName()?.getReferenceExpression()?.getReferencedName()
            )
        }

        val returnType = TypeInfo(element, Variance.OUT_VARIANCE)
        return FunctionInfo(OperatorConventions.INVOKE.asString(), receiverType, returnType, emptyList(), parameters)
    }
}
