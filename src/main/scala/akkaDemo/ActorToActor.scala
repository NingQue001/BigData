package akkaDemo

import akka.actor.{Actor, ActorSystem, Props}

object ActorToActor {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system")
    val actor1 = system.actorOf(Props[Actor1])
    actor1 ! "主函数main发给Actor1的消息"
  }
}

class Actor1 extends Actor {
  override def receive = {
    case msg: String => {
      println("Actor1 收到的消息是：" + msg)
      val b = context.actorOf(Props[Actor2])
      b ! "由Actor1发给Actor2的消息"
    }
  }
}
class Actor2 extends Actor {
  def receive = {
    case msg: String => {
      println("Actor2 收到的消息是：" + msg)
    }
  }
}