package fun

object FunTest {
  def main(args: Array[String]): Unit = {
    val arr = Array(1, 2, 4, 8)
    println(inject(arr, 0, (x, y) => x + y))
    println(inject2(arr, 0){ (x, y) => x + y } )

    // 实用scala原生函数
    // /：equal Array.foldLeft
    val sum = (0 /: arr) { _ + _ }
    println(s"sum: ${sum}")
    val max = (Integer.MIN_VALUE /: arr) { Math.max(_ , _) }
    println(s"max: ${max}")
    // 占位符：函数中只引用这个参数一次时，可使用占位符
    val isExisted = arr.exists(_ == 8)
    println(s"isExisted: ${isExisted}")
  }

  // 高阶函数
  def inject(arr: Array[Int], inital: Int, operator: (Int, Int) => Int) = {
    var carryOver = inital
    arr.foreach(element => carryOver = operator(carryOver, element))
    carryOver
  }
  // inject优化：柯里化（Currying）
  def inject2(arr: Array[Int], inital: Int)(operator: (Int, Int) => Int) = {
    var carryOver = inital
    arr.foreach(element => carryOver = operator(carryOver, element))
    carryOver
  }
}
