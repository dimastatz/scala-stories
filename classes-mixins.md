# Class composition with mixins
### Classes
Classes in Scala are blueprints for creating objects. A minimal class definition is simply the keyword class and an identifier.
```scala
class Point
val point = new Point
```
Often, constructor and class body are required.Members are public by default. Use the private access modifier to hide them from outside of the class.
```scala
class Point(x: Int, y: Int){
  def print: String = s"$x:$y"
}

val point = new Point(1, 1)
point.print
```
Parameters without val or var are private values, visible only within the class. It is possible to create public fields for ctor paramas
```scala
class Point(val x: Int, var y: Int){
  def print: String = s"$x:$y"
}
val point = new Point(1, 1)
point.y = 2
point.print
```
Constructor can have default values
```scala
class Point(var x: Int = 0, var y: Int = 0)
val point = new Point(y = 2)
```
