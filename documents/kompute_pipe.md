```kotlin
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
```
And using the komputive pipe will look something like this. 
```kotlin
fun someUsageCode() {
    @Komputive var input = 10
    val windowSize = 2
    val calc = someCalc(input, windowSize) //swap the return type to the generated interface in compiler pugin + ide plugin
    watch(calc.avg)() {
        val avg = calc.avg
        if (avg != null)
        println(avg)
    }
    val random = Random(0)
    (0 until 10).forEach {
        input = random.nextInt(100)
    }
}
```

In order for the above code to work we will need to also create an IDE plugin to help the ide know what usage of the komputive pipe looks like. 


## Generated [draft]
```kotlin
interface `KP$SomeCalc` {
    val input: Int
    val windowSize: Int
    val avg: Double?
    //Possibly generate some meta data about input or output. Or maybe atleast a designated return type?
    operator fun invoke(input: Int, windowSize: Int)
}
```
This function replaces any reference/usage of someCalc. This creates a dynamic container on the fly with all of the necessary properties defined in the komputive pipe format. The interface also gets a implementation of the calculation on the invoke operator. This will be used to invoke new updates to the reactive komputive pipe when its dependencies are reassigned. 
```kotlin
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
            avg = compute_avg(input, windowSize)
        }
    }
}
```

```kotlin
fun someUsageCode() {
    var input = 10
    val windowSize = 2
    val calc = someCalc(input, windowSize) //swap the return type to the generated interface in compiler pugin + ide plugin
    fun watch_calcAvg(avg : Double?) {
        if (avg != null)
        println(avg)
    }
    val random = Random(0)
    (0 until 10).forEach {
        input = random.nextInt(100)
        calc(input, windowSize)
        watch_calc(calc.avg)
    }
}
```
===

## State Objects

```kotlin
@Kompute
data class Window<T>(
        @Komputive
        val size: Int,
        @Komputive
        private val _elements: MutableList<T> = mutableListOf<T>()) {

    val elements: List<T> get() = _elements

    init {
        watch(_elements)() {
            while (_elements.size > size) _elements.removeAt(0)
        }
    }
}

@Kompute
fun window(input: Int, size: Int): Window<Int> {
    val elements = mutableListOf<Int>()
    @Komputive val window = Window<Int>(size = size, _elements = (elements + input).toMutableList())
    return window
}

fun example() {
    var input: Int = 10
    val periods = 7;
    val window = window(input, periods)
    val avg = window.elements.sum().toDouble() / window.size
    Watch {
        println("$avg")
    }
    val random = Random(0)
    (0 until 10).forEach {
        input = random.nextInt(100)
    }
}
```
