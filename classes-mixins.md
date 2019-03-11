# Class compositions with mixins
### Classes
Classes in Scala are blueprints for creating objects. A minimal class definition is simply the keyword class and an identifier. Constructors can have optional parameters by providing a default value.

```scala
// Default ctor
class Point
val point = new Point

// Ctor with default values
class Point(x: Int, y: Int)
val point = new Point(0,0)
```
