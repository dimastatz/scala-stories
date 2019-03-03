
# Implicit classes.
## Introduction
An implicit class is a class marked with the _implicit_ keyword. This keyword makes the class’s primary constructor available for implicit conversions when the class is in scope. For example, rather than create a separate library of String utility methods, like a StringUtilities class, you want to add your own behavior(s) to the String class. This will let you write code like this:

```scala
// Extension method
"HAL".increment

// Utilities class
StringUtilities.increment("HAL")
```
