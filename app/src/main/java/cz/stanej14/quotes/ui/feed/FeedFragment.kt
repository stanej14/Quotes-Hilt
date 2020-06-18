package cz.stanej14.quotes.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.stanej14.quotes.R
import cz.stanej14.quotes.domain.error.ErrorHandler
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.ui.main.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feed.*
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
    }

    @Inject
    lateinit var errorHandler: ErrorHandler

    private val viewModel: FeedViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels({ requireActivity() })

    private val quotesAdapter = QuotesAdapter(object : QuotesCallback {
        override fun onTagClicked(tag: String) {
            viewModel.onTagClicked(tag)
        }

        override fun onQuoteClicked(quote: Quote) {
            navigationViewModel.navigateToQuoteDetail(quote)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_feed.setOnRefreshListener { viewModel.onRefresh() }
        btn_feed_login.setOnClickListener { navigationViewModel.navigateToLogin() }
        search_feed.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onQueryUpdated(newText)
                return true
            }

        })
        recycler_feed.adapter = quotesAdapter

        viewModel.quotes.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success<List<Quote>> -> showQuotes(it.data)
                is Resource.Error -> showError(it.error)
                is Resource.Loading -> showLoading()
            }
        })
    }

    private fun showLoading() {
        refresh_feed.isRefreshing = true
    }

    private fun showError(error: Throwable) {
        refresh_feed.isRefreshing = false
        errorHandler.handleError(requireView(), error)
    }

    private fun showQuotes(data: List<Quote>) {
        refresh_feed.isRefreshing = false
        empty_feed.isVisible = data.isEmpty()
        quotesAdapter.setData(data)
    }
}