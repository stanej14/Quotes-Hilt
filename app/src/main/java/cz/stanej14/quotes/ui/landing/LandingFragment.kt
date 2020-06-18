package cz.stanej14.quotes.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.stanej14.quotes.R
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.domain.error.ErrorHandler
import cz.stanej14.quotes.ui.main.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_landing.*
import javax.inject.Inject

@AndroidEntryPoint
class LandingFragment : Fragment() {

    @Inject
    lateinit var errorHandler: ErrorHandler

    companion object {
        fun newInstance() = LandingFragment()
    }

    private val viewModel: LandingViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_landing, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_landing.setOnRefreshListener {
            viewModel.onRefresh()
        }
        btn_landing_next.setOnClickListener {
            navigationViewModel.navigateInside()
        }

        viewModel.quote.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success<Quote> -> showQuote(it.data)
                is Resource.Error -> showError(it.error)
                is Resource.Loading -> showLoading()
            }
        })
    }

    private fun showLoading() {
        refresh_landing.isRefreshing = true
    }

    private fun showError(error: Throwable) {
        refresh_landing.isRefreshing = false
        errorHandler.handleError(requireView(), error)
    }

    private fun showQuote(data: Quote) {
        refresh_landing.isRefreshing = false
        quote_landing.apply {
            setOnClickListener { navigationViewModel.navigateToQuoteDetail(data) }
            isVisible = true
            initialise(data)
        }
    }
}