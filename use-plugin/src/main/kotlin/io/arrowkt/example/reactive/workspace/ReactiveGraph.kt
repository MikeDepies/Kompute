package io.arrowkt.example.reactive.workspace

import kotlin.random.Random

//metadebug
@Komputive
fun main() {
    var input: String = "some user input"
    val upperInput: String = input.toUpperCase()
    val validInput: Boolean = input.length < 20
    //..
    println("input : $input \nuppercasedInput : $upperInput \nvalidInput : $validInput")
    //reasign the input
    input = "much longer invalid input"
    println("input : $input \nuppercasedInput : $upperInput \nvalidInput : $validInput")
    Watch {
        println("some $input was entered and uppercased: $upperInput")
    }
    Watch(watch(input, upperInput)) {
        //do some stuff
    }
    watch(input, upperInput)() {

    }
    //windowing tool:
    //fold(input) { }
    //buffer(x,y) { }
    //
}

@Komputive
fun test() {
    var input = 0
//    val avg =
}
//watch(a,b,c) andDo { }
//annotation class Watch

fun someUsageCode() {
    var input = 10
    val windowSize = 2
    val calc = `@someCalc`(input, windowSize)
    "${calc.avg}".also {
        val avg = calc.avg
        if (avg != null)
        println(avg)
    }
    val random = Random(0)
    (0 until 10).forEach {
        input = random.nextInt(100)
    }
}

/**
watch(calc.avg)() {
    val avg = calc.avg
    if (avg != null)
    println(avg)
}
 */

@Komputive
fun someCalc(input: Int, windowSize: Int): Double? {
    //initalized values are for first call
    val elements = mutableListOf<Int>()
    //how to signal that this is suppose ot happen on each successive change
    //Do block for each time a input or windowSize changes {
    elements += input
    if (elements.size > windowSize) elements.remove(0)
    val avg: Double? = if (elements.size < windowSize) null else elements.sum() / windowSize.toDouble()
    return avg
    // }
}

@Komputive
fun `@someCalc`(input: Int, windowSize: Int): `KP$SomeCalc` {
    return initalizeSomeCalc(input, windowSize)
    // }
}


interface `KP$SomeCalc` {
    val input: Int
    val windowSize: Int
    val avg: Double?
    //Possibly generate some meta data about input or output. Or maybe atleast a designated return type?
    operator fun invoke(input: Int, windowSize: Int)
}

fun initalizeSomeCalc(input: Int, windowSize: Int): `KP$SomeCalc` {
    val elements = mutableListOf<Int>()
    fun compute_avg(input: Int, windowSize: Int): Double? {
        return if (elements.size < windowSize) null else elements.sum() / windowSize.toDouble()
    }
    return object : `KP$SomeCalc` {
        override var input: Int = input
        override var windowSize: Int = windowSize
        override var avg: Double? = compute_avg(input, windowSize)
        override operator fun invoke(input: Int, windowSize: Int) {
            elements += input
            if (elements.size > windowSize) elements.remove(0)
            avg = if (elements.size < windowSize) null else elements.sum() / windowSize.toDouble()
        }
    }
}
/*

fun original() {
    var a = 1
    val b = 1 + a
    println(b)
    a += 10
    println(b)
}

fun t1() {
    fun compute_b(a: Int) = 1 + a
    var a = 1
    var b = compute_b(a)
    println(b)
    a += 10
    b = compute_b(a)
    println(b)

}

fun original2() {
    var a = 1
    var b = 1
    val c = 1 + a
    val d = b + c
    val str = """Data:
        |a = $a
        |b = $b
        |c = $c
        |d = $d
    """.trimMargin()
    println(str)
    a += 10
    println(str)
    b = 10
    println(str)
}


fun t2() {
    fun compute_c(a: Int) = 1 + a
    fun compute_d(b: Int, c: Int) = b + c
    fun compute_str(a: Int, b: Int, c: Int, d: Int) = """Data:
        |a = $a
        |b = $b
        |c = $c
        |d = $d
    """.trimMargin()

    var a = 1
    var b = 1
    var c = compute_c(a)
    var d = compute_d(b, c)
    var str = compute_str(a,b, c, d)
    println(str)
    a += 10
    c = compute_c(a)
    d = compute_d(b, c)
    str = compute_str(a,b, c, d)
    println(str)
    b = 10
    d = compute_d(b, c)
    str = compute_str(a,b, c, d)
    println(str)
}*/
