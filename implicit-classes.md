
# Implicit classes
### Introduction
An implicit class is a class marked with the _implicit_ keyword (from Scala 2.10). This keyword makes the class’s primary constructor available for implicit conversions when the class is in scope. For example, rather than create a separate library of Int utility methods, like a IntUtils class, you can add your own behavior(s) to the Int class. This will let you write code like this:

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
There is another way to define implicit classes. We can create implicit function which returns anonymous object with _square_ implemenattion. It will work, and the code looks clean, and a bit shorter. 

```scala
object MyExtensions {
  implicit def richInt(i: Int) = new {
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

But such implementation will be kind of performance killer. Let's look into compiled code

```scala
package <empty> {
  object MyExtensions extends Object {
    implicit def richInt(i: Int): Object = new <$anon: Object>(i);
    def <init>(): MyExtensions.type = {
      MyExtensions.super.<init>();
      ()
    }
  };
  object UsingExtension extends Object {
    <static> def <init>: Unit = {
      UsingExtension.reflParams$Cache1 = Array[Class]{};
      UsingExtension.reflPoly$Cache1 = new ref.SoftReference(new runtime.EmptyMethodCache());
      ()
    };
    final <synthetic> <static> private var reflParams$Cache1: Array[Class] = Array[Class]{};
    @volatile <synthetic> <static> private var reflPoly$Cache1: ref.SoftReference = new ref.SoftReference(new runtime.EmptyMethodCache());
    <synthetic> <static> def reflMethod$Method1(x$1: Class): reflect.Method = {
      var methodCache1: runtime.MethodCache = UsingExtension.reflPoly$Cache1.get().$asInstanceOf[runtime.MethodCache]();
      if (methodCache1.eq(null))
        {
          methodCache1 = new runtime.EmptyMethodCache();
          UsingExtension.reflPoly$Cache1 = new ref.SoftReference(methodCache1)
        };
      var method1: reflect.Method = methodCache1.find(x$1);
      if (method1.ne(null))
        return method1
      else
        {
          method1 = scala.runtime.ScalaRunTime.ensureAccessible(x$1.getMethod("square", UsingExtension.reflParams$Cache1));
          UsingExtension.reflPoly$Cache1 = new ref.SoftReference(methodCache1.add(x$1, method1));
          return method1
        }
    };
    // TODO: !!! reflection involved - performance killer
    def run(): Int = {
      val x: Int = 5;
      scala.Int.unbox({
        val qual1: Object = MyExtensions.richInt(x);
        try {
  UsingExtension.reflMethod$Method1(qual1.getClass()).invoke(qual1, Array[Object]{})
} catch {
  case (1 @ (_: reflect.InvocationTargetException)) => throw 1.getCause()
}.$asInstanceOf[Integer]()
      })
    };
    def <init>(): UsingExtension.type = {
      UsingExtension.super.<init>();
      ()
    }
  };
  final class anon$1 extends Object {
    def square(): Int = anon$1.this.i$1.*(anon$1.this.i$1);
    <synthetic> <paramaccessor> private[this] val i$1: Int = _;
    def <init>(i$1: Int): <$anon: Object> = {
      anon$1.this.i$1 = i$1;
      anon$1.super.<init>();
      ()
    }
  }
}
```

As you can see it will compiler infers a structural type for the _richInt_ method, and invokes square method by using reflection.


