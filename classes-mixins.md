# Class composition with mixins
### Classes
Classes in Scala are blueprints for creating objects. A minimal class definition is simply the keyword _class_ and an identifier.
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

### Traits
Traits are used to share interfaces and fields between classes. They are similar to Java interfaces. Classes and objects can extend traits but traits cannot be instantiated and therefore have no parameters.
```scala
trait Shape {
  def getArea: Double
}
```
Use the extends keyword to extend a trait. Then implement any abstract members of the trait using the override keyword.
```scala
trait Shape {
  def getArea: Double
}

class Point(x: Int, y: Int) extends Shape {
  override def getArea: Double = 0
}

class Rectangle(p: Point, height: Int, width: Int) extends Shape {
  override def getArea: Double = height * width
}
```
Trait can extend another trait and trait's methods can have default implementation
```scala
trait ColoredShape extends Shape {
  def getColor: String = "Black"
}
```
### Mixins
Mixins are traits which are used to compose a class. For example, see __ColoredSquare__ which extends Rectangle and have ColoredShape as mixin.
```scala
trait Shape {
  def getArea: Double
}
trait ColoredShape extends Shape {
  def getColor: String = "Black"
}
class Point(x: Int, y: Int) extends Shape {
  override def getArea: Double = 0
}
class Rectangle(p: Point, height: Int, width: Int) extends Shape {
  override def getArea: Double = height * width
}
class ColoredSquare(p: Point, size: Int) extends Rectangle(p, size, size) with ColoredShape {
}
val square = new ColoredSquare(new Point(1,1), 2)
square.getArea
square.getColor
```
It is possible to use mixins on the fly 
```scala
val redPoint = new {} with Point(1, 2) with ColoredShape {
  override def getColor: String = "red"
}
redPoint.getColor
```
Resolving conflicting members
```scala
trait Shape {
  def getName: String = "Shape"
  def getArea: Double = 0
}
trait ColoredShape {
  def getName: String = "ColoredShape"
  def getColor: String = "Black"
}
// Will fail if getName is not overrided
class Point extends Shape with ColoredShape {
}
val p = new Point
p.getName
```


