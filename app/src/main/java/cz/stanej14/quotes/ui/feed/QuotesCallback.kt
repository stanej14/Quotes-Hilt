package cz.stanej14.quotes.ui.feed

import cz.stanej14.quotes.model.Quote

interface QuotesCallback {
    fun onTagClicked(tag: String)
    fun onQuoteClicked(quote: Quote)
}