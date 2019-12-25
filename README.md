# Kompute
Kotlin Compiler plugin bringing reactivity to variable declarations.

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
