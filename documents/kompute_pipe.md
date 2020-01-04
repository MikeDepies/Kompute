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
