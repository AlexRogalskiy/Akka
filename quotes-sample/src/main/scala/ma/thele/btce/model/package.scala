package ma.thele.btce

import spray.json.DefaultJsonProtocol

/**
 * @author Evgeniy Muravev
 * @since 22.09.2015.
 */
package object model {

  case class Ticker(high: BigDecimal,
                    low: BigDecimal,
                    avg: BigDecimal,
                    vol: BigDecimal,
                    last: BigDecimal,
                    buy: BigDecimal,
                    sell: BigDecimal,
                    updated: Long,
                    server_time: Long)

  case class Quote(ticker: Ticker)

  object BtceJsonProtocol extends DefaultJsonProtocol {
    implicit val tickerFormat = jsonFormat9(Ticker)

    implicit val quoteFormat = jsonFormat1(Quote)
  }

}
