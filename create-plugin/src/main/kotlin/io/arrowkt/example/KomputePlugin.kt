package io.arrowkt.example

import arrow.meta.Meta
import arrow.meta.Plugin
import arrow.meta.invoke
import arrow.meta.phases.CompilerContext
import arrow.meta.quotes.Transform.Companion.replace
import arrow.meta.quotes.namedFunction
import arrow.meta.quotes.nameddeclaration.stub.typeparameterlistowner.NamedFunction
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtVariableDeclaration
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType

val Meta.kompute: Plugin
    get() =
        "Kompute" {
            val compilerContext = this
            meta(
                    namedFunction({ annotationEntries.any { it.text == "@Komputive" }}) { e ->
                        val dictionary = DictionaryImpl(mutableMapOf(), mutableMapOf(), mutableMapOf())
                        val children = value.getChildrenOfType<PsiElement>()
                        val variableDeclarations = value.collectDescendantsOfType<KtVariableDeclaration>()
                        variableDeclarations.forEach { dictionary.registerReactiveElement(it) }
                        val assignment = variableDeclarations.map { "$it" }
                        val functions = dictionary.reactiveElements.map { element ->
                            val dependencies = dictionary.extractReactiveElements(element.declaration)
                            if (element.elementType == ReactiveElementType.Val) dependencies.forEach { dependency -> dictionary.addDependency(element, dependency) }
                            createComputeFun(element, dependencies)
                        }
                        val newBody = body?.value?.children?.fold("") { acc, psiElement -> buildBody(psiElement, dictionary, acc) }
                        val newScope = """fun $name $`(params)` {
                            |   println("${e.annotationEntries.first().text}")
                            |   ${functions.fold("") { acc, namedFunction -> "$acc \n $namedFunction" }}
                            |   $newBody
                            |}
                        """.trimMargin()
                        replace(e, newScope.function)
                    }
            )

        }

private fun CompilerContext.buildBody(psiElement: PsiElement, dictionary: DictionaryImpl, acc: String): String {
    return when {
        psiElement is KtVariableDeclaration && psiElement.name.toString() in dictionary -> {
            "$acc \n ${psiElement.text.replace("val ", "var ")}"
        }
        psiElement is KtBinaryExpression && dictionary[psiElement.left!!.text].elementType == ReactiveElementType.Var -> {
            fun recursiveSearch(element: ReactiveElement, collected: Set<ReactiveElement>): Set<ReactiveElement> {
                val elements = dictionary.dependentsForElement(element)
                return if (elements.isEmpty()) setOf()
                else collected + elements.flatMap { recursiveSearch(it, collected) } + elements
            }

            val element: ReactiveElement = dictionary[psiElement.left!!.text]
            //sort useComputeFunc by element declaration order. This is a "free" way to get dependency ordering
            val dependentElements = recursiveSearch(element, setOf())
            val useFunctions = dependentElements.sortedBy { it.id }.map { useComputeFun(it, dictionary.dependenciesForElement(it)) }.fold("") { acc, s -> "$acc \n $s" }
            "$acc \n ${psiElement.text} \n $useFunctions"
        }
        else -> "$acc \n ${psiElement.text}"
    }
}

fun CompilerContext.createComputeFun(element: ReactiveElement, dependencies: Set<ReactiveElement>): NamedFunction { //.also { println("compute_${element.name}") }
    return """fun compute_${element.name}(${dependencies.joinToString(", ") { "${it.name} : ${it.type}" }}) : ${element.type} = ${element.declaration.initializer?.text}""".trimMargin().function
}

fun CompilerContext.useComputeFun(element: ReactiveElement, dependencies: Set<ReactiveElement>): String {
    return """${element.name} = compute_${element.name}(${dependencies.joinToString(", ") { it.name }})""".trimMargin()
}

fun Dictionary.extractReactiveElements(expression: KtExpression?): Set<ReactiveElement> {
    val references = expression?.collectDescendantsOfType<KtReferenceExpression>()
    return references
            ?.filter { it.text in this }
            ?.map { this[it.text] }?.toSet()
            ?: setOf<ReactiveElement>()
}