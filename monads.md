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
It looks flat and clean, but has ```scala if (x == null) return 0``` pattern which returns too many times. We can simplify it by using exceptions:
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
