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

package org.jetbrains.kotlin.idea.completion

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementWeigher
import com.intellij.codeInsight.lookup.WeighingContext
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.VariableDescriptor
import org.jetbrains.kotlin.idea.completion.smart.SMART_COMPLETION_ITEM_PRIORITY_KEY
import org.jetbrains.kotlin.idea.completion.smart.SmartCompletionItemPriority
import org.jetbrains.kotlin.idea.completion.smart.fuzzyTypesForSmartCompletion
import org.jetbrains.kotlin.idea.core.SmartCastCalculator
import org.jetbrains.kotlin.idea.core.completion.DeclarationLookupObject
import org.jetbrains.kotlin.idea.util.FuzzyType
import org.jetbrains.kotlin.idea.util.ImportInsertHelper
import org.jetbrains.kotlin.idea.util.makeNotNullable
import org.jetbrains.kotlin.idea.util.nullability
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.platform.JavaToKotlinClassMap
import org.jetbrains.kotlin.psi.JetFile
import org.jetbrains.kotlin.resolve.ImportPath
import org.jetbrains.kotlin.types.typeUtil.TypeNullability
import java.util.HashSet

object PriorityWeigher : LookupElementWeigher("kotlin.priority") {
    override fun weigh(element: LookupElement, context: WeighingContext)
            = element.getUserData(ITEM_PRIORITY_KEY) ?: ItemPriority.DEFAULT
}

object SmartCompletionPriorityWeigher : LookupElementWeigher("kotlin.smartCompletionPriority") {
    override fun weigh(element: LookupElement, context: WeighingContext)
            = element.getUserData(SMART_COMPLETION_ITEM_PRIORITY_KEY) ?: SmartCompletionItemPriority.DEFAULT
}

object KindWeigher : LookupElementWeigher("kotlin.kind") {
    private enum class Weight {
        variable, // variable or property
        function,
        keyword,
        default,
        packages
    }

    private data class CompoundWeight(val weight: Weight, val callableWeight: CallableWeight? = null) : Comparable<CompoundWeight> {
        override fun compareTo(other: CompoundWeight): Int {
            if (callableWeight != null && other.callableWeight != null && callableWeight != other.callableWeight) {
                return callableWeight.compareTo(other.callableWeight)
            }
            return weight.compareTo(other.weight)
        }
    }

    override fun weigh(element: LookupElement): CompoundWeight {
        val o = element.getObject()

        return when (o) {
            is PackageLookupObject -> CompoundWeight(Weight.packages)

            is DeclarationLookupObject -> {
                val descriptor = o.descriptor
                when (descriptor) {
                    is VariableDescriptor -> CompoundWeight(Weight.variable, element.getUserData(CALLABLE_WEIGHT_KEY))
                    is FunctionDescriptor -> CompoundWeight(Weight.function, element.getUserData(CALLABLE_WEIGHT_KEY))
                    else -> CompoundWeight(Weight.default)
                }
            }

            is KeywordLookupObject -> CompoundWeight(Weight.keyword)

            else -> CompoundWeight(Weight.default)
        }
    }
}

object DeprecatedWeigher : LookupElementWeigher("kotlin.deprecated") {
    override fun weigh(element: LookupElement): Int {
        val o = element.getObject() as? DeclarationLookupObject ?: return 0
        return if (o.isDeprecated) 1 else 0
    }
}

object PreferMatchingItemWeigher : LookupElementWeigher("kotlin.preferMatching", false, true) {
    private enum class Weight {
        keywordExactMatch,
        defaultExactMatch,
        functionExactMatch,
        notExactMatch
    }

    override fun weigh(element: LookupElement, context: WeighingContext): Weight {
        val prefix = context.itemPattern(element)
        if (element.lookupString != prefix) {
            return Weight.notExactMatch
        }
        else {
            val o = element.`object`
            return when (o) {
                is KeywordLookupObject -> Weight.keywordExactMatch
                is DeclarationLookupObject -> if (o.descriptor is FunctionDescriptor) Weight.functionExactMatch else Weight.defaultExactMatch
                else -> Weight.defaultExactMatch
            }
        }
    }
}

class DeclarationRemotenessWeigher(private val file: JetFile) : LookupElementWeigher("kotlin.declarationRemoteness") {
    private val importCache = ImportCache()

    private enum class Weight {
        thisFile,
        kotlinDefaultImport,
        preciseImport,
        allUnderImport,
        default,
        hasImportFromSamePackage,
        notImported,
        notToBeUsedInKotlin
    }

    override fun weigh(element: LookupElement): Weight {
        val o = element.getObject() as? DeclarationLookupObject ?: return Weight.default

        val elementFile = o.psiElement?.getContainingFile()
        if (elementFile is JetFile && elementFile.getOriginalFile() == file) {
            return Weight.thisFile
        }

        val fqName = o.importableFqName
        if (fqName != null) {
            val importPath = ImportPath(fqName, false)

            if (o is PackageLookupObject) {
                return when {
                    importCache.isImportedWithPreciseImport(fqName) -> Weight.preciseImport
                    else -> Weight.default
                }
            }

            return when {
                JavaToKotlinClassMap.INSTANCE.mapPlatformClass(fqName).isNotEmpty() -> Weight.notToBeUsedInKotlin
                ImportInsertHelper.getInstance(file.getProject()).isImportedWithDefault(importPath, file) -> Weight.kotlinDefaultImport
                importCache.isImportedWithPreciseImport(fqName) -> Weight.preciseImport
                importCache.isImportedWithAllUnderImport(fqName) -> Weight.allUnderImport
                importCache.hasPreciseImportFromPackage(fqName.parent()) -> Weight.hasImportFromSamePackage
                else -> Weight.notImported
            }
        }

        return Weight.default
    }

    private inner class ImportCache {
        private val preciseImports = HashSet<FqName>()
        private val preciseImportPackages = HashSet<FqName>()
        private val allUnderImports = HashSet<FqName>()

        init {
            for (import in file.getImportDirectives()) {
                val importPath = import.getImportPath() ?: continue
                val fqName = importPath.fqnPart()
                if (importPath.isAllUnder()) {
                    allUnderImports.add(fqName)
                }
                else {
                    preciseImports.add(fqName)
                    preciseImportPackages.add(fqName.parent())
                }
            }
        }

        fun isImportedWithPreciseImport(name: FqName) = name in preciseImports
        fun isImportedWithAllUnderImport(name: FqName) = name.parent() in allUnderImports
        fun hasPreciseImportFromPackage(packageName: FqName) = packageName in preciseImportPackages
    }
}

//TODO: prefer by name too (and only by name too)
class ExpectedInfoMatchWeigher(
        private val expectedInfos: Collection<ExpectedInfo>,
        private val smartCastCalculator: SmartCastCalculator
) : LookupElementWeigher("kotlin.expectedInfoMatch") {
    private enum class Weight {
        matches,
        matchesIfNotNull,
        doesNotMatch
    }

    override fun weigh(element: LookupElement): Weight {
        // TODO: keywords with type
        val o = element.`object`
        val fuzzyTypes = when (o) {
            is DeclarationLookupObject -> {
                val descriptor = o.descriptor ?: return Weight.doesNotMatch
                descriptor.fuzzyTypesForSmartCompletion(smartCastCalculator)
            }

            is ThisItemLookupObject -> smartCastCalculator.types(o.receiverParameter).map { FuzzyType(it, emptyList()) }

            else -> return Weight.doesNotMatch
        }

        return when {
            fuzzyTypes.any { type -> expectedInfos.any { it.matchingSubstitutor(type) != null } } -> Weight.matches
            fuzzyTypes.any { type -> type.nullability() == TypeNullability.NULLABLE && expectedInfos.any { it.matchingSubstitutor(type.makeNotNullable()) != null } } -> Weight.matchesIfNotNull
            else -> Weight.doesNotMatch
        }
    }
}