package io.arrowkt.example

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtVariableDeclaration

/**
 * DependencyMap -> what elements a given element uses/relies on
 * DependentMap -> what elements rely on given element (inverse lookup)
 */
class DictionaryImpl(
        private val mutableMap: MutableMap<String, ReactiveElement>,
        private val dependencyMap: MutableMap<ReactiveElement, Set<ReactiveElement>>,
        private val dependentMap: MutableMap<ReactiveElement, Set<ReactiveElement>>
) : Dictionary {
    private var id = 0
    private val computeFunctions = mutableSetOf<ReactiveElement>()
    override fun registerReactiveElement(element: KtVariableDeclaration) {
        fun t(element: KtExpression?): String {
            val lookup = mapOf(
                    "INTEGER_CONSTANT" to "Int",
                    "BOOLEAN_CONSTANT" to "Boolean"
            )

            fun f(e: PsiElement?): String? {
                return when {
                    e != null && e.children.isNotEmpty() -> element?.children?.map {
                        f(it)
                    }?.first()
                    e is KtConstantExpression -> if ("$e" == "FLOAT_CONSTANT") {
                        if (e.textContains('f')) "Float" else "Double"
                    } else lookup[e.elementType.toString()]
                    else -> throw RuntimeException("cant find type $e")
                }
            }

            return f(element) ?: ""
        }

        val name = element.name ?: throw RuntimeException("cant find type")
        val type = element.typeReference?.text
                ?: t(element)
        val elementType = if (element.isVar) ReactiveElementType.Var else ReactiveElementType.Val
        mutableMap[name] = ReactiveElement(id++, name, type, element, elementType)

    }

    override fun addDependency(element: ReactiveElement, dependency: ReactiveElement) {
        val dependenciesForElement = dependencyMap.computeIfAbsent(element) { mutableSetOf<ReactiveElement>() }
        dependencyMap[element] = dependenciesForElement + dependency
        val dependentsForElement = dependentMap.computeIfAbsent(dependency) { mutableSetOf<ReactiveElement>() }
        dependentMap[dependency] = dependentsForElement + element
    }

    override fun dependenciesForElement(element: ReactiveElement): Set<ReactiveElement> {
        return dependencyMap[element] ?: setOf() //kind of bad to return empty probably should throw exception
        // and have a boolean checker for more explicit code
    }

    override fun dependentsForElement(element: ReactiveElement): Set<ReactiveElement> {
        return dependentMap[element] ?: setOf()
    }

    override val reactiveElements: Set<ReactiveElement>
        get() = mutableMap.values.toSet()

    override fun contains(name: String): Boolean {
        return mutableMap.contains(name)
    }

    override fun get(name: String): ReactiveElement {
        return mutableMap[name]!!
    }

}