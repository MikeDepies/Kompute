package io.arrowkt.example.reactive.workspace

object Watch {
    operator fun invoke(dependencies : WatchContext = ExtractDependencies, block : () -> Unit) : Unit= TODO()
}

fun watch(vararg property : Any?) : WatchContext = TODO()

interface WatchContext
object ExtractDependencies : WatchContext
operator fun WatchContext.invoke(block: () -> Unit) : Unit = TODO()
infix fun WatchContext.andDo(block: () -> Unit) : Unit = TODO()