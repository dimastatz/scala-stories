
# Implicit classes
### Introduction
An implicit class is a class marked with the _implicit_ keyword (from Scala 2.10). This keyword makes the class’s primary constructor available for implicit conversions when the class is in scope. For example, rather than create a separate library of Int utility methods, like a IntUtils class, you want to add your own behavior(s) to the Int class. This will let you write code like this:

```scala
val x: Int = 3

// Extension
x.square

// Utilities
IntUtils.square(x)
```

### Example
Let's created implcit class RichInt and implement there extension methods for Int.   

```scala
object MyExtensions {
  implicit class RichInt(i: Int) {
    def square: Int = i * i
 }
}
 
object UsingExtension {
  import MyExtensions._
  def run(): Int = {
    val x: Int = 5
    x.square
  }
}
```

### How it works 
So how compiler translates implcit classes? 

```scala
package <empty> {
  object MyExtensions extends Object {
    implicit <synthetic> def RichInt(i: Int): MyExtensions$RichInt = new MyExtensions$RichInt(i);
    def <init>(): MyExtensions.type = {
      MyExtensions.super.<init>();
      ()
    }
  };
  object UsingExtension extends Object {
    // TODO: !!! no reflection, overhead of creating new object
    def run(): Int = {
      val x: Int = 5;
      MyExtensions.RichInt(x).square()
    };
    def <init>(): UsingExtension.type = {
      UsingExtension.super.<init>();
      ()
    }
  };
  implicit class MyExtensions$RichInt extends Object {
    <paramaccessor> private[this] val i: Int = _;
    def square(): Int = MyExtensions$RichInt.this.i.*(MyExtensions$RichInt.this.i);
    def <init>(i: Int): MyExtensions$RichInt = {
      MyExtensions$RichInt.this.i = i;
      MyExtensions$RichInt.super.<init>();
      ()
    }
  }
}
```
