package cz.stanej14.quotes.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import cz.stanej14.quotes.R
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.ui.feed.QuotesCallback
import kotlinx.android.synthetic.main.widget_quote.view.*

class QuoteWidget : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_quote, this, true)
    }

    fun initialise(quote: Quote, callback: QuotesCallback? = null) {
        chips_quote.removeAllViews()
        text_quote_author.text = quote.author
        text_quote_body.text = quote.body
        separator_quote.isVisible = quote.tags.isNotEmpty()
        quote.tags.map { tag ->
            Chip(context).apply {
                text = tag
                if (callback != null) {
                    isEnabled = true
                    setOnClickListener { callback.onTagClicked(tag) }
                } else {
                    isEnabled = false
                }
            }
        }.forEach {
            chips_quote.addView(it)
        }
    }
}