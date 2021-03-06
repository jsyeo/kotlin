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

package org.jetbrains.kotlin.idea.search.ideaExtensions

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifierListOwner
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.Processor
import com.intellij.util.QueryExecutor
import com.intellij.util.indexing.FileBasedIndex
import org.jetbrains.kotlin.asJava.LightClassUtil
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.stubindex.JetAnnotationsIndex
import org.jetbrains.kotlin.idea.util.application.runReadAction
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode

public class KotlinAnnotatedElementsSearcher : QueryExecutor<PsiModifierListOwner, AnnotatedElementsSearch.Parameters> {

    override fun execute(p: AnnotatedElementsSearch.Parameters, consumer: Processor<PsiModifierListOwner>): Boolean {
        val annClass = p.getAnnotationClass()
        assert(annClass.isAnnotationType(), "Annotation type should be passed to annotated members search")

        val annotationFQN = annClass.getQualifiedName()
        assert(annotationFQN != null)

        val useScope = p.getScope()

        for (elt in getJetAnnotationCandidates(annClass, useScope)) {
            if (notJetAnnotationEntry(elt)) continue

            val result = runReadAction(fun(): Boolean {
                val parentOfType = elt.getStrictParentOfType<JetDeclaration>() ?: return true

                val annotationEntry = elt as JetAnnotationEntry

                val context = annotationEntry.analyze(BodyResolveMode.PARTIAL)
                val annotationDescriptor = context.get(BindingContext.ANNOTATION, annotationEntry) ?: return true

                val descriptor = annotationDescriptor.getType().getConstructor().getDeclarationDescriptor() ?: return true
                if (!(DescriptorUtils.getFqName(descriptor).asString() == annotationFQN)) return true

                if (parentOfType is JetClass) {
                    val lightClass = LightClassUtil.getPsiClass(parentOfType as JetClass?)
                    if (!consumer.process(lightClass)) return false
                }
                else if (parentOfType is JetNamedFunction || parentOfType is JetSecondaryConstructor) {
                    val wrappedMethod = LightClassUtil.getLightClassMethod(parentOfType as JetFunction)
                    if (!consumer.process(wrappedMethod)) return false
                }
                return true
            })
            if (!result) return false
        }

        return true
    }

    companion object {
        private val LOG = Logger.getInstance("#com.intellij.psi.impl.search.AnnotatedMembersSearcher")

        /* Return all elements annotated with given annotation name. Aliases don't work now. */
        private fun getJetAnnotationCandidates(annClass: PsiClass, useScope: SearchScope): Collection<PsiElement> {
            return runReadAction(fun(): Collection<PsiElement> {
                if (useScope is GlobalSearchScope) {
                    val name = annClass.getName() ?: return emptyList()
                    return JetAnnotationsIndex.getInstance().get(name, annClass.getProject(), useScope)
                }

                return (useScope as LocalSearchScope).scope.flatMap { it.collectDescendantsOfType<JetAnnotationEntry>() }
            })
        }

        private fun notJetAnnotationEntry(found: PsiElement): Boolean {
            if (found is JetAnnotationEntry) return false

            val faultyContainer = PsiUtilCore.getVirtualFile(found)
            LOG.error("Non annotation in annotations list: $faultyContainer; element:$found")
            if (faultyContainer != null && faultyContainer.isValid()) {
                FileBasedIndex.getInstance().requestReindex(faultyContainer)
            }

            return true
        }
    }

}
