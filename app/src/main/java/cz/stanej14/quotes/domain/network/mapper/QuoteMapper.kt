package cz.stanej14.quotes.domain.network.mapper

import cz.stanej14.quotes.domain.network.quotes.QuoteDto
import cz.stanej14.quotes.model.Quote

class QuoteMapper {

    fun map(quotes: List<QuoteDto>) = quotes.mapNotNull { map(it) }

    fun map(quote: QuoteDto): Quote? {
        if (quote.author == null) return null

        return Quote(
            id = quote.id,
            tags = quote.tags,
            body = quote.body,
            author = quote.author,
            isFavorite = quote.userDetails?.favorite ?: false
        )
    }
}