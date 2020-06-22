package cz.stanej14.quotes.ui.landing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.stanej14.quotes.domain.landing.ObserveLandingQuoteUseCase
import cz.stanej14.quotes.domain.util.cancelIfActive
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LandingViewModel @ViewModelInject constructor(
    private val observeLandingQuoteUseCase: ObserveLandingQuoteUseCase
) : ViewModel() {

    private var observeLandingQuoteJob: Job? = null
    private val _quote = MutableLiveData<Resource<Quote>>(Resource.Loading)
    val quote: LiveData<Resource<Quote>> = _quote

    init {
        startObservingLandingQuote()
    }

    fun onRefresh() {
        startObservingLandingQuote()
    }

    private fun startObservingLandingQuote() {
        observeLandingQuoteJob.cancelIfActive()
        observeLandingQuoteJob = viewModelScope.launch {
            _quote.value = Resource.Loading
            observeLandingQuoteUseCase.observeLandingQuote().collect { resource ->
                _quote.postValue(resource)
            }
        }
    }
}