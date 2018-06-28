package ma.thele.bter

import akka.actor.{Actor, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import ma.thele.bter.model.{BterJsonProtocol, Quote}
import ma.thele.domain.messages.Tick
import ma.thele.domain.model.{Quote => DomainQuote}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Evgeniy Muravev
 * @since 23.09.2015.
 */
class BterCrawler extends Actor with SprayJsonSupport {
  import BterJsonProtocol._

  implicit val system = context.system

  implicit val mater = ActorMaterializer()

  def receive = {
    case Tick =>
      val responseFuture: Future[HttpResponse] =
        Http().singleRequest(HttpRequest(uri = "http://data.bter.com/api/1/ticker/BTC_USD"))

      responseFuture flatMap { response =>
        Unmarshal(response.entity).to[Quote]
      } onSuccess { case quote =>
        val domainQuote = DomainQuote(bid = quote.sell,
          ask = quote.buy,
          source = "BTER",
          timestamp = System.currentTimeMillis()
        )

        context.parent ! domainQuote
      }

  }

}

object BterCrawler {
  def props = Props[BterCrawler]
}
