package ma.thele.quote

import ma.thele.domain.model.Quote

import scala.collection.mutable

/**
 * @author Evgeniy Muravev
 * @since 22.09.2015.
 */
object QuoteModule {

  trait QuoteService {
    def list: Seq[Quote]

    def put(quote: Quote): Unit
  }

  val quotes = new QuoteService {
    val storage = mutable.Buffer.empty[Quote]

    def list: Seq[Quote] = storage

    def put(quote: Quote) = { storage += quote }
  }

}
