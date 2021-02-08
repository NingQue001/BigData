package basic

object CasePro {
  def main(args: Array[String]): Unit = {
    for (list <- Array(List(0), List(1, 0), List(88), List(0, 0, 0), List(1, 0, 0))) {
      val result = list match {
        case 0 :: Nil => "0" //
        case x :: value :: tail => x + "value:" + value + "tail: " +tail //
        case 0 :: tail => "0 ..." //
        case x :: Nil => x
        case _ => "something else"
      }
      // println(result)
    }

    // 神仙操作：case中置表达式
    List(1, 7, 4, 9) match {
      case first :: second :: rest => println(first +" " + second + " "   + rest.length)
      case _ => 0
    }

  }
}
