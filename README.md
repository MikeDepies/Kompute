# Kompute
Kotlin Compiler plugin bringing reactivity to variable declarations.
### Example:
```
@Komputive fun main() {
    var a = 1.0
    val b = 1 + a
    val str: String = """Data:
        |a = $a
        |b = $b
    """.trimMargin()
    val print: Unit = { println(str) }()
    a += 10
    a = 100
    a = -5
} 
```

gets translated to:
```fun main() {
    fun compute_b(a : Int) = 1 + a
    fun compute_str(a: Int, b:Int) = """Data:
        |a = $a
        |b = $b
    """.trimMargin()
    fun compute_print(str : String) = { println(str) }()
    var a = 1.0
    val b = 1 + a
    val str: String = """Data:
        |a = $a
        |b = $b
    """.trimMargin()
    val print: Unit = { println(str) }()
    a += 10
    b= compute_b(a)
    str = compute_str(a,b)
    print = compute_print(str)
    a = 100
    
    b= compute_b(a)
    str = compute_str(a,b)
    print = compute_print(str)
    a = -5
    
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


This is a work in progress and is currently serving as a learning project.

## TODO
- Type inference: Need to figure out how to resolve a type on a declaration without explicit Type.
- Propper compute watcher function syntax: I'd like to formalize a way to declare watcher functions instead of a hacky syntax like the one currently used.
- Reactive Classes: Seems logical to extend this behavior to classes to create reactive data objects
- Handle variable declarations without initalizers?
- Stop hoisting var declarations since they are not reactive to other properties.
