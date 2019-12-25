//package io.arrowkt.example.reactive.workspace
//
//import kotlin.properties.Delegates
//
//class ReactiveValue<T>(v: T) {
//    private val listeners = mutableListOf<(T) -> Unit>()
//
//    var value: T by Delegates.observable(v) { property, oldValue, newValue ->
//        if (oldValue != newValue) notifyListeners(newValue)
//    }
//
//    fun addListener(block: (T) -> Unit) {
//        listeners += block
//    }
//
//    private fun notifyListeners(value: T) {
//        listeners.forEach { it(this.value) }
//    }
//
//    operator fun invoke(value: T) {
//        this.value = value
//    }
//
//    operator fun invoke() = value
//}
//
//class ComputedValue<T>(private val function: () -> T) {
//    private val listeners = mutableListOf<(T) -> Unit>()
//    private var v by Delegates.observable(function()) { property, oldValue, newValue ->
//        println("observe on D")
//        if (oldValue != newValue) notifyListeners(newValue)
//    }
//    val value get() = v
//    operator fun invoke() {
//        v = function()
//    }
//    fun addListener(block: (T) -> Unit) {
//        listeners += block
//    }
//
//    private fun notifyListeners(value: T) {
//        listeners.forEach { it(this.value) }
//    }
//}
//
//fun main() {
//    //var a = 0
//    val a = ReactiveValue(0)
//    //var b = 0
//    val b = ReactiveValue(0)
//    //val c = a + b
//    val function = { a.value + b.value }
//    val c = ComputedValue(function)
//    val d = ComputedValue {
//        a.value + c.value
//    }
//
//
//    fun p() {
//        println("""
//            a = ${a.value}
//            b = ${b.value}
//            c = ${c.value}
//            d = ${d.value}
//            = = = = = = = =
//        """.trimIndent())
//    }
//
//    a.addListener { c.invoke() }
//    b.addListener { c.invoke() }
//    a.addListener { d.invoke() }
//    c.addListener { d.invoke() }
//d.addListener { p() }
//    a.value = 2
//    a.value = 4
//    b.value= 10
//}