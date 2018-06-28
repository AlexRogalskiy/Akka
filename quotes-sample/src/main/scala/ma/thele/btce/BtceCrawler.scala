package ma.thele.btce

import akka.actor.{Actor, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpCharsets, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import ma.thele.btce.model.{BtceJsonProtocol, Quote}
import ma.thele.domain.messages.Tick
import ma.thele.domain.model.{Quote => DomainQuote}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Evgeniy Muravev
 * @since 21.09.2015.
 */
class BtceCrawler extends Actor with SprayJsonSupport {
  import BtceJsonProtocol._

  implicit val system = context.system

  implicit val mater = ActorMaterializer()

  def receive = {
    case Tick =>
      val responseFuture: Future[HttpResponse] =
        Http().singleRequest(HttpRequest(uri = "https://btc-e.com/api/2/btc_usd/ticker"))

      responseFuture flatMap { response =>
        Unmarshal(response.entity.withContentType(ContentTypes.`application/json`.withCharset(HttpCharsets.`UTF-8`)))
          .to[Quote]
      } onSuccess { case Quote(ticker) =>
        val domainQuote = DomainQuote(bid = ticker.sell,
          ask = ticker.buy,
          source = "BTC-E",
          timestamp = ticker.updated * 1000 // converting to millis
        )

        context.parent ! domainQuote
      }

  }

}

object BtceCrawler {
  def props = Props[BtceCrawler]
}


