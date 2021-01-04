package akkaDemo

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Worker {
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = "9999"
    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
         |""".stripMargin
    )
    val actorSystem = ActorSystem("WorkerSystem", config)
    val worker = actorSystem.actorOf(Props[Worker], "Worker")
  }
}

class Worker extends Actor {

  var master : ActorSelection = _
  override def preStart(): Unit = {
    master = context.actorSelection("akka.tcp://MasterSystem@127.0.0.1:8888/user/Master")
    master ! "connect"
  }

  override def receive: Receive = {
    case "reply" => {
      println("a reply from master")
    }
  }
}
