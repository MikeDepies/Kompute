package io.arrowkt.example

import org.jetbrains.kotlin.psi.KtVariableDeclaration

interface Dictionary {
    fun registerReactiveElement(element: KtVariableDeclaration)
    fun addDependency(element: ReactiveElement, dependency: ReactiveElement)
    fun dependenciesForElement(element: ReactiveElement): Set<ReactiveElement>
    fun dependentsForElement(element: ReactiveElement): Set<ReactiveElement>
    operator fun contains(name: String): Boolean
    operator fun get(name: String): ReactiveElement
    val reactiveElements: Set<ReactiveElement>
}