# Kompute
Kotlin Compiler plugin bringing reactivity to variable declarations.

This is a work in progress and is currently serving as a learning project. The basic effect is to be able to be able to write variable declarations that automatically watch for changes in any variable that would cause its value to be stale.

So for example if we take a simple declaration:
```kotlin
@Komputive fun main() {
    var input : String = "some user input"
    val upperInput : String = input.toUpperCase()
    val validInput : Boolean = input.length < 20
    //..
    println("input : $input \nuppercasedInput : $upperInput \nvalidInput : $validInput")
    //reasign the input
    input = "much longer invalid input"
    println("input : $input \nuppercasedInput : $upperInput \nvalidInput : $validInput")
}
```

In this contrived example we set up some relationships with our variables. As our root state changes, input, our derivative states, upperInput and validInput, automatically update. Below is the console output from the above code.
```kotlin
@Komputive
input : some user input 
uppercasedInput : SOME USER INPUT 
validInput : true
input : much longer invalid input 
uppercasedInput : MUCH LONGER INVALID INPUT 
validInput : false
```

### Example:
```kotlin
@Komputive fun main() {
    var a : Double = 1.0
    val b : Double = 1 + a
    val str: String = """Data:
        |a = $a
        |b = $b
    """.trimMargin()
    val print: Unit = { println(str) }()
    a += 10
    a = 100.0
    a = -5.0
}
```

produces the following console output:
```
Data:
a = 1.0
b = 2.0
Data:
a = 11.0
b = 12.0
Data:
a = 100.0
b = 101.0
Data:
a = -5.0
b = -4.0
```

The compiler plugin translates the example code into the following
```kotlin
fun main() {
    fun compute_b(a : Double) = 1 + a
    fun compute_str(a: Double, bDoubleInt) = """Data:
        |a = $a
        |b = $b
    """.trimMargin()
    fun compute_print(str : String) = { println(str) }()
    var a : Double = 1.0
    var b : Double = 1 + a
    var str: String = """Data:
        |a = $a
        |b = $b
    """.trimMargin()
    var print: Unit = { println(str) }()
    a += 10.0
    b= compute_b(a)
    str = compute_str(a,b)
    print = compute_print(str)
    a = 100.0
    
    b= compute_b(a)
    str = compute_str(a,b)
    print = compute_print(str)
    a = -5.0
    
    b= compute_b(a)
    str = compute_str(a,b)
    print = compute_print(str)
} 
```

Current Status:
Currently the plugin is targeting any function with the @Komputive annotation. The plug searches for var and val declarations in the function body. Hoists their initializer into a function. Captures some metadata about identifiers, types and dependencies. Then the plugin finds each assignment of a reactive var and calls a computation function for each dependent declaration. This process propagates untill all values have been inlined to update. 

Current Watcher function syntax is:

  ```val someWatcher : Unit = { println(someWatchedVar) }()```
  
  Honestly the currently syntax is very flexible and will pick up usages of other variables and react to their changes. So a format like this could also exist. 
  
```val someWatcher : String = "$a $b $c".also { /*do some side effect */ } ```

The biggest issue with this is that you must execute right away. This can be remedied with a more formal solution.

#### Potential Syntax for Watch Functions
```kotlin
Watch { //automatic hoisting of dependencies used in block function
    println("some $input was entered and uppercased: $upperInput")
}
Watch(watch(input, upperInput)) { //explicity defined variables to watch
    //do some stuff
}
watch(input, upperInput) andDo { //explicity defined variables to watch

}
```



## TODO
- Type inference: Need to figure out how to resolve a type on a declaration without explicit Type.
- Propper compute watcher function syntax: I'd like to formalize a way to declare watcher functions instead of a hacky syntax like the one currently used.
- Reactive Classes: Seems logical to extend this behavior to classes to create reactive data objects
- Handle variable declarations without initalizers?
- ~~Stop hoisting var declarations since they are not reactive to other properties.~~
- memoization to prevent needless update propagations
- create counterpart ide plugin: Provide markers to help reveal the dependency behavior.
- More finegrain control on what is "komputive" and what is not. 
    - Suggestion[moriturius]: allow particular declaration statements be targeted with the @komputive annotation
