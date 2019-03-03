
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
Looks great! Clean readable code. And the next question how it works, how compiler translates implicit classes? Does it have performance overhead? Let's look on compiled code by running _scalac -Xprint:all_

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
As you can see, pretty straightforward. Compiler created an object with implicit method _RichInt_ which returns _RichInt_ class. Because compiler cannot find square method in _Int_ class, it replaces int by call to _RichInt_ method. Almost 0 impact on performance.


### The bad way to define _implicit_ classes





