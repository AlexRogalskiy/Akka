package ma.thele

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ma.thele.domain.QuoteAggregator
import ma.thele.domain.model.DomainJsonProtocol
import ma.thele.quote.QuoteModule

import scala.io.StdIn

/**
 * @author Evgeniy Muravev
 * @since 21.09.2015.
 */
object AppStarter extends App with SprayJsonSupport {
  import DomainJsonProtocol._

  implicit val system = ActorSystem("btc-quotes")

  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val route = (path("quotes") & get) {
    complete {
      QuoteModule.quotes.list
    }
  } ~ pathPrefix("") {
    pathEndOrSingleSlash {
      getFromResource("static/index.html")
    }
  }

  val apiHost = "127.0.0.1"

  val apiPort = 8080

  val quoteAggregator = system.actorOf(QuoteAggregator.props)

  val bindingFuture = Http().bindAndHandle(route, apiHost, apiPort)

  println(s"Server started at http://$apiHost:$apiPort/\nPress `Enter` to stop...")

  StdIn.readLine()

  bindingFuture.flatMap(_.unbind())

  Http().shutdownAllConnectionPools().onComplete{ _ =>
    system.shutdown()
  }
}
