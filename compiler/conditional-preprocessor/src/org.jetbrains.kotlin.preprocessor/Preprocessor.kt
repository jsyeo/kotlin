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

package org.jetbrains.kotlin.preprocessor

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.JetFileType
import org.jetbrains.kotlin.psi.*
import java.io.File


fun main(args: Array<String>) {
    require(args.size() == 1, "Please specify path to sources")

    val sourcePath = File(args.first())

    val configuration = CompilerConfiguration()
    val environment = KotlinCoreEnvironment.createForProduction(Disposable {  }, configuration, emptyList())

    val project = environment.project
    val jetPsiFactory = JetPsiFactory(project)
    val fileType = JetFileType.INSTANCE

    val evaluators = listOf(JvmPlatformEvaluator(version = 7), JsPlatformEvaluator())



    println("Using condition evaluator: $evaluators")

    (FileTreeWalk(sourcePath) as Sequence<File>)
            .filter { it.isFile && it.extension == fileType.defaultExtension }
            .forEach { sourceFile ->
                val sourceText = sourceFile.readText().convertLineSeparators()
                val psiFile = jetPsiFactory.createFile(sourceFile.name, sourceText)
                println("$psiFile")

                val visitor = CollectModificationsVisitor(evaluators)
                psiFile.accept(visitor)
                val modifications = visitor.elementModifications

                for ((evaluator, list) in modifications) {
                    if (list.isNotEmpty()) {
                        val resultText = applyModifications(list, sourceText, evaluator)
                        println("Version of $sourceFile for $evaluator")
                        println(resultText)
                    }
                }
            }
}

private fun applyModifications(modifications: List<Modification>, sourceText: String, evaluator: Evaluator): String {
    var prevIndex = 0
    val result = StringBuilder()
    for ((range, selector) in modifications) {
        result.append(sourceText, prevIndex, range.startOffset)
        val rangeText = range.substring(sourceText)
        val newValue = selector(rangeText)
        if (newValue.isEmpty()) {
            result.append("/* Not available on $evaluator */")
            repeat(StringUtil.getLineBreakCount(rangeText)) {
                result.append("\n")
            }
        }
        else {
            result.append(newValue)
        }
        prevIndex = range.endOffset
    }
    result.append(sourceText, prevIndex, sourceText.length())
    val resultT = result.toString()
    return resultT
}


data class Modification(val range: TextRange, val selector: (String) -> String)

class CollectModificationsVisitor(evaluators: List<Evaluator>) : JetTreeVisitorVoid() {

    val elementModifications: Map<Evaluator, MutableList<Modification>> =
            evaluators.toMap(selector = { it }, transform = { arrayListOf<Modification>() })

    override fun visitDeclaration(declaration: JetDeclaration) {
        super.visitDeclaration(declaration)

        val annotations = declaration.parseConditionalAnnotations()
        val name = (declaration as? JetNamedDeclaration)?.nameAsSafeName ?: declaration.name

        val declResults = arrayListOf<Pair<Evaluator, Boolean>>()
        for ((evaluator, modifications) in elementModifications) {
            val conditionalResult = evaluator(annotations)
            declResults.add(evaluator to conditionalResult)

            if (!conditionalResult)
                modifications.add(Modification(declaration.textRange) {""})
            else {
                val targetName = annotations.filterIsInstance<Conditional.TargetName>().singleOrNull()
                if (targetName != null) {
                    val placeholderName = (declaration as JetNamedDeclaration).nameAsName!!.asString()
                    val realName = targetName.name
                    modifications.add(Modification(declaration.textRange) { it.replace(placeholderName, realName) })
                }
            }

        }
        println("declaration: ${declaration.javaClass.simpleName} $name${if (annotations.isNotEmpty()) ", annotations: ${annotations.joinToString { it.toString() }}, evaluation result: $declResults" else ""}")
    }
}

fun String.convertLineSeparators(): String = StringUtil.convertLineSeparators(this)



