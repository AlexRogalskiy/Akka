package ma.thele.bter

import spray.json.DefaultJsonProtocol

/**
 * @author Evgeniy Muravev
 * @since 23.09.2015.
 */
package object model {

  case class Quote(buy: BigDecimal,
                   sell: BigDecimal,
                   last: BigDecimal,
                   high: BigDecimal,
                   low: BigDecimal,
                   avg: BigDecimal)

  object BterJsonProtocol extends DefaultJsonProtocol {
    implicit val quoteFormat = jsonFormat6(Quote)
  }

}
