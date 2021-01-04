package akkaDemo

import akka.actor.{Actor, ActorSystem, Props}

/**
 * Akka framwork，并行，容错和可扩展的RPC（Remote Procedure Call）框架，RPC是进程间的通讯
 * 基于Actor模型，ActorSystem中的Actor互相发送异步消息，每个Actor都拥有一个mailbox来存储接收到的message
 * 发送消息的线程是共享的，ActorSystem维护着一个线程池
 */
object ActorToActor {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("system")
    val actor1 = actorSystem.actorOf(Props[Actor1])
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