## Kompute Classes

```kotlin
@Kompute
class SomeCalculationTemplate(var input1 : Int, var input 2: Int) {
        val sum = input1 + input 2
        val window = 
                Window(
                    size = 4,
                    elements = mutableListOf<Int>(input).collect { it += input }
                )
        val avg = window.elements.average()
}

@Kompute
fun example(data : List<Pair<Int, Int>) {
        @Komputive
        val x = 0
        @Komputive
        val y = 0
        
        @Komputive
        val calc = SomeCalculationTemplate(x,y)
         
        //non-existent charting api
        watch(calc.avg) { plot(calv.avg) }
        //read data into x & y
        for(d in data) {
                x = it.first
                y = it.second
        }
}
```
===
## State Objects

```kotlin

@Kompute
data class Window<T>(
        val size: Int,
        val elements: MutableList<T>) {
    init {
        watch(elements)() {
            while (elements.size > size) elements.removeAt(0)
        }
    }
}

infix fun <T> T.collect(acc: (T) -> Unit): T = this
infix fun <T> T.accumulate(acc: (T) -> T): T = this

fun example() {
    @Komputive
    var input: Int = 10
    @Komputive
    val window =
            Window(
                    size = 4,
                    elements = mutableListOf<Int>(input).collect { it += input }
            )
    val avg = window.elements.average()
    watch(avg)() {
        println("$avg")
    }
    //push data through
    val random = Random(0)
    (0 until 10).forEach {
        input = random.nextInt(100)
    }
}
```
Compiler generates the following
```kotlin

fun kompute_example() {
    fun `collect_window(elements,input)`(elements: MutableList<Int>, input: Int) {
        elements += input
    }
    fun compute_avg(window: Kompute_Window<Int>) = window.elements.sum().toDouble() / window.size
    fun `compute_watch(avg)`(avg: Double) {
        println("$avg")
    }

    @Komputive var input: Int = 10
    @Komputive val window = Kompute_Window<Int>(
            size = 4,
            elements = mutableListOf<Int>())
    var avg = compute_avg(window)
    `compute_watch(avg)`(avg)
    //push data through
    val random = Random(0)
    (0 until 10).forEach {
        input = random.nextInt(100)
        `collect_window(elements,input)`(window.elements, input)
        window.`compute_watch(elements)`(window.elements)
        avg = compute_avg(window)
        `compute_watch(avg)`(avg)
    }
}


@Kompute
data class Kompute_Window<T>(
        val size: Int,
        val elements: MutableList<T>) {
    init {
        watch(elements)() {
            while (elements.size > size) elements.removeAt(0)
        }
    }
    fun `compute_watch(elements)`(elements: MutableList<T>) {
        while (elements.size > size) elements.removeAt(0)
    }
}
```
