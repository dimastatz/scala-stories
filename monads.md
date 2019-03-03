# Monads

### Problem scope
Consider following code:

```scala
class Document {
  def getActivePage: Page = ???
}
class Page {
  def getSelectedText: String = ???
}
```
The goal is to implement _getSelectedTextLength_ method which returns the length of selected text on active page, otherwise it returns 0. The simple aproach is to implement it as follows:

```scala
// coping with failures
def getSelectedTextLength(doc: Document): Int = {
  if(doc != null) {
    val page = doc.getActivePage
    if(page != null){
      val text = page.getSelectedText
      if(text != null){
        text.length
      }
      else 0
    }
    else 0
  }
  else 0
}
```
Such implementation is OK, but it has unpleasent level of nested indentation. There is another way to implement it
```scala
def getSelectedTextLength(doc: Document): Int = {
  if(doc == null)
    return 0
 
  val page = doc.getActivePage
  if(page == null)
    return 0
 
  val text = page.getSelectedText
  if(text == null)
    return 0
 
  text.length
}
```
It looks flat and clean, but has ```if (x == null) return 0``` pattern which returns too many times. We can simplify it by using exceptions:
```scala
def getSelectedTextLength(doc: Document): Int = {
  try {
    doc.getActivePage.getSelectedText.length
  }
  catch {
    case _: NullPointerException => 0
    case e: Exception => throw e
  }
}
```
This version looks good,but has some problem though. If _NullPointerException_ is thrown from _getActivePage_ or _getSelectedText_ it will be unintentionally handled by our code and by doing so, our code will hide potential bug.

### Monads
Monad is a design pattern that allows structuring programs generically while automating away boilerplate code needed by the program logic and provides an easy way for composing and sequencing operations on some contained value(s).

```scala
trait Monad[A] {
  def map[B](f: A => B): Monad[B]
  def flatMap[B](f: A => Monad[B]): Monad[B]
}
```

### Option monad
Option monad wrappes value of any given type and have two specific implementation: None when value is not exist (null) or Some for existing value.

```scala
trait Option[A] {
  def map[B](f: A => B): Option[B]
  def flatMap[B](f: A => Option[B]): Option[B]
}
 
case class None[A]() extends Option[A] {
  def map[B](f: A => B): Option[B] = new None
  def flatMap[B](f: A => Option[B]): Option[B] = new None
}
 
case class Some[A](a: A) extends Option[A] {
  def map[B](f: A => B): Option[B] = {
    Some(f(a))
  }
  def flatMap[B](f: A => Option[B]): Option[B] = {
    f(a)
  }
}
```
Let's rewrite our example by using Option monad.
```scala
class Document {
  def getActivePage: Option[Page] = ???
}
class Page {
  def getSelectedText: Option[String] = ???
}
 
def getSelectedTextLength(doc: Option[Document]): Int = {
  doc
    .flatMap(_.getActivePage)
    .flatMap(_.getSelectedText)
    .map(_.length).getOrElse(0)
}
```

