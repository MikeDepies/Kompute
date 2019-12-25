package io.arrowkt.example

import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType


fun processKtElement(expression: KtExpression?): String {
    return when (expression) {
        is KtBinaryExpression -> extractSymbolsFromBinaryExpression(expression)
        is KtConstantExpression -> "constant[${expression.text}]"
        is KtReferenceExpression -> "reference[${expression.text}]"
        is KtParenthesizedExpression -> parenthesized(expression)
        else -> "$expression"
    }
}

fun extractSymbolsFromBinaryExpression(expression: KtBinaryExpression): String {
    return "${processKtElement(expression.left)} ${expression.operationToken} ${processKtElement(expression.right)}"
}

fun parenthesized(expression: KtParenthesizedExpression): String {
    val joinToString = expression.collectDescendantsOfType<KtBinaryExpression>().joinToString { processKtElement(it) }
    return "($joinToString)"
}