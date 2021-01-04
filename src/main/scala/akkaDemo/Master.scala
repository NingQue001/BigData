package akkaDemo

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Master {
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = "8888"
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
         |""".stripMargin
    )
    val actorSystem = ActorSystem("MasterSystem", config)
    val master = actorSystem.actorOf(Props[Master], "Master")
    master ! "hello"
  }
}

class Master extends Actor {
  println("constructor invoked")


  override def preStart(): Unit = {
    println("preStart invoked")
  }

  override def receive: Receive = {
    case "connect" => {
      println("a client connect")
      sender ! "reply" // 回复Worker
    }
    case "hello" => {
      println("a clinet say hello")
    }
    case "ok" => {
      println("a clinet say ok")
    }
  }
}
