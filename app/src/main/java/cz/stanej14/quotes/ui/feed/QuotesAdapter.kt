package cz.stanej14.quotes.ui.feed

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.ui.widget.QuoteWidget

class QuotesAdapter(private val callback: QuotesCallback) :
    RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    private val data = mutableListOf<Quote>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val quoteWidget = QuoteWidget(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }
        return ViewHolder(quoteWidget, callback)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setData(_data: List<Quote>) {
        data.clear()
        data.addAll(_data)
        notifyDataSetChanged()
    }

    class ViewHolder(private val quoteWidget: QuoteWidget, private val callback: QuotesCallback) :
        RecyclerView.ViewHolder(quoteWidget) {
        fun bind(quote: Quote) {
            with(quoteWidget) {
                setOnClickListener { callback.onQuoteClicked(quote) }
                initialise(quote, callback)
            }
        }
    }
}