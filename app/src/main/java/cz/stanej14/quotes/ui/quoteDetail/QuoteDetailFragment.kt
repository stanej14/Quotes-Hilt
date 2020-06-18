package cz.stanej14.quotes.ui.quoteDetail

import android.os.Bundle
import android.util.EventLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import cz.stanej14.quotes.R
import cz.stanej14.quotes.domain.error.ErrorHandler
import cz.stanej14.quotes.domain.livedata.EventObserver
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.ui.main.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_quote_detail.*
import javax.inject.Inject

@AndroidEntryPoint
class QuoteDetailFragment : Fragment() {

    @Inject
    lateinit var errorHandler: ErrorHandler

    companion object {
        private const val KEY_QUOTE = "quote"

        fun newInstance(quote: Quote) = QuoteDetailFragment().apply {
            arguments = bundleOf(KEY_QUOTE to quote)
        }
    }

    private val quote: Quote by lazy {
        requireArguments().getParcelable(KEY_QUOTE) as? Quote
            ?: throw IllegalStateException("QuoteDetailFragment created without a quote!")
    }

    private val viewModel: QuoteDetailViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_quote_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.initialize(quote)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        btn_quote_detail_favorite.setOnClickListener { viewModel.onFavoriteClicked() }

        viewModel.quote.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> renderQuote(it.data)
                is Resource.Loading -> showLoading()
                is Resource.Error -> handleError(it.error)
            }
        })
        viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver {
            navigationViewModel.navigateToLogin()
        })
    }

    private fun handleError(error: Throwable) {
        loading_quote_detail.isVisible = false
        btn_quote_detail_favorite.isVisible = true
        errorHandler.handleError(requireView(), error)
    }

    private fun showLoading() {
        loading_quote_detail.isVisible = true
        btn_quote_detail_favorite.isVisible = false
    }

    private fun renderQuote(quote: Quote) {
        chips_quote_detail.removeAllViews()
        btn_quote_detail_favorite.isVisible = true
        loading_quote_detail.isVisible = false
        with(quote) {
            text_quote_detail_author.text = author
            text_quote_detail_body.text = body

            val r = if (isFavorite) R.drawable.ic_favorite_off else R.drawable.ic_favorite_on
            btn_quote_detail_favorite.setImageResource(r)

            tags.forEach { tag ->
                chips_quote_detail.addView(
                    Chip(context).apply {
                        text = tag
                        isEnabled = false
                    }
                )
            }
        }
    }
}