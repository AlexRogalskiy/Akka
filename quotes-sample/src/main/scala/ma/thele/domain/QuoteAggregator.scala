package ma.thele.domain

import akka.actor.{Actor, ActorLogging, Props}
import ma.thele.btce.BtceCrawler
import ma.thele.bter.BterCrawler
import ma.thele.domain.messages.Tick
import ma.thele.domain.model.{Quote => DomainQuote}
import ma.thele.quote.QuoteModule

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
 * @author Evgeniy Muravev
 * @since 22.09.2015.
 */
class QuoteAggregator extends Actor with ActorLogging {

  val children = Seq(BtceCrawler.props, BterCrawler.props) map { context.actorOf }

  val tick = context.system.scheduler.schedule(2 seconds, 5 seconds, self, Tick)

  override def postStop() = tick.cancel()

  def receive = {
    case Tick =>
      children foreach { child =>
        child ! Tick
      }

    case dq: DomainQuote =>
      log.info(s"Got a quote: $dq")
      QuoteModule.quotes.put(dq)

  }

}

object QuoteAggregator {
  def props = Props[QuoteAggregator]
}
