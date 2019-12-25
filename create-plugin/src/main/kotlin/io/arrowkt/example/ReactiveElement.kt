package io.arrowkt.example

import org.jetbrains.kotlin.psi.KtVariableDeclaration

data class ReactiveElement(val id: Int, val name: String, val type: String, val declaration: KtVariableDeclaration, val elementType: ReactiveElementType)