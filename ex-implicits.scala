// define implicit class
object MyExtensions {
  implicit class RichInt(i: Int) {
    def square: Int = i * i
 }
}
 
 
// use extension method
object UsingExtension {
  import MyExtensions._
  def run(): Int = {
    val x: Int = 5
    x.square
  }
}
