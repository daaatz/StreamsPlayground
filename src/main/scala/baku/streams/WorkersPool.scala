package baku.streams

import akka.NotUsed
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._

object WorkersPool extends App {

  implicit val system = ActorSystem("Main")
  implicit val materializer = ActorMaterializer()

  def balancer[In, Out](worker: Flow[In, Out, NotUsed], workerCount: Int): Flow[In, Out, NotUsed] = {
    import GraphDSL.Implicits._

    Flow.fromGraph(GraphDSL.create() { implicit b =>
      val balancer = b.add(Balance[In](workerCount))
      val merge = b.add(Merge[Out](workerCount))

      for (_ <- 1 to workerCount) {
        balancer ~> worker ~> merge
      }
      FlowShape(balancer.in, merge.out)
    })
  }

  val flow = Flow[Int].map(e => {
    println(s"NUM: $e Thread ID: ${Thread.currentThread().getId}")
    Thread.sleep(1000)
    e
  }).async

  Source((1 to 1000))
    .via(balancer(flow, 3))
    .runForeach(_ => ())

}
