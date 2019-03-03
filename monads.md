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
The goal is to implement __getSelectedTextLength__ method which returns the length of selected text on active page, otherwise it returns 0.

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
