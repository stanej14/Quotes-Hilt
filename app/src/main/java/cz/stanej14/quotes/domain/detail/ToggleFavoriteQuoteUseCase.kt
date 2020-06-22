package cz.stanej14.quotes.domain.detail

import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource

interface ToggleFavoriteQuoteUseCase {
    suspend fun toggleFavorite(quote: Quote): Resource<Unit>
}