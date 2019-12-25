package io.arrowkt.example.reactive.workspace

//metadebug

@Komputive fun main() {
    var a = 1.0
    var b = 1
    val c: Double = 1 + a
    val d: Double = b + c
    val str: String = """Data:
        |a = $a
        |b = $b
        |c = $c
        |d = $d
    """.trimMargin()
    val print: Unit = { println(str) }()
    a += 10
    b = 10
    a = b.toDouble()
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
