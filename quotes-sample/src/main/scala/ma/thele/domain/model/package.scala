package ma.thele.domain

import spray.json.DefaultJsonProtocol

/**
 * @author Evgeniy Muravev
 * @since 22.09.2015.
 */
package object model {

  case class Quote(bid: BigDecimal,
                   ask: BigDecimal,
                   source: String,
                   timestamp: Long)

  object DomainJsonProtocol extends DefaultJsonProtocol {
    implicit val quoteFormat = jsonFormat4(Quote)

    implicit val quoteSeqFormat = seqFormat[Quote](quoteFormat)
  }

}
